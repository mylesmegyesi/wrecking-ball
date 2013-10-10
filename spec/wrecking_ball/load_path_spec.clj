(ns wrecking-ball.load-path-spec
  (:require [speclj.core             :refer :all]
            [wrecking-ball.load-path :refer :all]))

(describe "wrecking-ball.load-path"

  (with wd-relative "spec/wrecking_ball/test_load_path")
  (with cp-relative "wrecking_ball/test_load_path")

  (it "reads a file when the load path is relative to the working directory"
    (let [content (read-file-from-load-path "test1" [@wd-relative] ["txt"])]
      (should= "i'm a txt file\n" (:body content))
      (should= "test1.txt" (:file-name content))
      (should= "txt" (:extension content))))

  (it "reads a file when the load path is relative to the class path"
    (let [content (read-file-from-load-path "test1" [@cp-relative] ["txt"])]
      (should= "i'm a txt file\n" (:body content))
      (should= "test1.txt" (:file-name content))
      (should= "txt" (:extension content))))

  )
