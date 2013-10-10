(ns wrecking-ball.load-path
  (:require [clojure.java.io      :refer [file as-url]]
            [cemerick.pomegranate :refer [get-classpath]])
  (:import [java.io  FileNotFoundException]
           [java.net URL MalformedURLException URLClassLoader]))

(defn- add-trailing-slash [path]
  (if (.endsWith path "/")
    path
    (str path "/")))

(defn- jar-entries [jar-url]
  (.entries (.getJarFile (.openConnection jar-url))))

(def ^:private files-in-jar
  (memoize
    (fn [jar-url]
      (loop [entries (jar-entries jar-url) results []]
        (if (.hasMoreElements entries)
          (recur entries (conj results (.getName (.nextElement entries))))
          results)))))

(defn- search-jar [base-url path]
  (let [jar-url (URL. "jar" "" (str base-url "!/"))
        entries (files-in-jar jar-url)]
    (some
      (fn [entry]
        (when (.startsWith entry path)
          (add-trailing-slash (str jar-url path))))
      (files-in-jar jar-url))))

(defn- search-fs [base-path path]
  (let [f (file (str base-path "/" path))]
    (when (and (.exists f) (.isDirectory f))
      (add-trailing-slash (str (.toURI f))))))

(defn- search-in-path [base-url path]
  (let [base-path (.getPath base-url)]
    (if (.endsWith base-path ".jar")
      (search-jar base-url path)
      (search-fs base-path path))))

(defn- search-class-path [next]
  (fn [path]
    (let [cp (map as-url (get-classpath))]
      (if-let [found (some #(search-in-path % path) cp)]
        found
        (next path)))))

(defn- search-working-directory [next]
  (fn [path]
    (let [f (file path)]
      (if (and (.exists f) (.isDirectory f))
        (str (.toURI f))
        (next path)))))

(defn not-found [path]
  (throw (Exception. (str "Could not find directory " path " in the working directory or in the class path"))))

(def absolute-path-with-protocol (-> not-found
                                   search-class-path
                                   search-working-directory))

(defn- read-file [class-loader file-name extension]
  (let [file-name-with-extension (str file-name "." extension)]
    (when-let [stream (.getResourceAsStream class-loader file-name-with-extension)]
      {:body (slurp stream)
       :extension extension
       :file-name file-name-with-extension})))

(def class-loader-for-load-paths
  (memoize
    (fn [load-paths]
      (let [urls (map #(-> % absolute-path-with-protocol as-url) load-paths)]
        (URLClassLoader. (into-array URL urls))))))

(defn read-file-from-load-path [file-name load-paths extensions]
  (let [class-loader (class-loader-for-load-paths load-paths)]
    (some identity (map (partial read-file class-loader file-name) extensions))))
