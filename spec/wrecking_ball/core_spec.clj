(ns wrecking-ball.core-spec
  (:require [speclj.core        :refer :all]
            [wrecking-ball.core :refer :all]))

(describe "wrecking-ball.core"

  (it "default context"
    (should= nil (:layout *view-context*))
    (should= [] (:load-paths *view-context*))
    (should= [] (:helpers *view-context*)))

  (context "with test templates"

    (around [it]
      (binding [*view-context* (assoc *view-context* :load-paths ["spec/wrecking_ball/test_view"])]
        (it)))

    (it "renders a hiccup template with extensions .hiccup.clj or .hiccup"
      (let [hiccup (render-template "hiccup-template")
            hiccup-clj (render-template "hiccup-template2")]
        (should= "<p>hello from hiccup!</p>" hiccup)
        (should= "<p>hello from hiccup2!</p>" hiccup-clj)))

    (it "renders a fleet template with extensions .html.fleet or .fleet"
      (let [fleet (render-template "fleet-template")
            html-fleet (render-template "fleet-template2")]
        (should= "<p>hello from fleet!</p>\n" fleet)
        (should= "<p>hello from fleet2!</p>\n" html-fleet)))

    (it "renders a hiccup template inside a hiccup layout"
      (let [html (render-template "hiccup-template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>hello from hiccup!</p></body></html>" html)))

    (it "renders a hiccup template inside a fleet layout"
      (let [html (render-template "hiccup-template" :layout "fleet-layout")]
        (should= "<html><head><title>Fleet Layout</title></head><body><p>hello from hiccup!</p></body></html>\n" html)))

    (it "renders a fleet template inside a fleet layout"
      (let [html (render-template "fleet-template" :layout "fleet-layout")]
        (should= "<html><head><title>Fleet Layout</title></head><body><p>hello from fleet!</p>\n</body></html>\n" html)))

    (it "renders a fleet template inside a hiccup layout"
      (let [html (render-template "fleet-template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>hello from fleet!</p>\n</body></html>" html)))

    (it "renders a template nested in a directory"
      (let [html (render-template "nested/nested_template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>nested_template</p></body></html>" html)))

    (it "provides a nice error message when template is missing"
      (should-throw
        Exception
        "Template Not Found: non-existent\nextensions: [\"hiccup\" \"hiccup.clj\" \"html.fleet\" \"fleet\"]\nload path:  [\"spec/wrecking_ball/test_view\"]"
        (render-template "non-existent")))

    )

  )
