(ns wrecking-ball.core-spec
  (:require [speclj.core        :refer :all]
            [wrecking-ball.core :refer :all]))

(describe "wrecking-ball.core"

  (around [it]
    (binding [*view-context* (assoc *view-context* :load-paths ["spec/wrecking_ball/test_view"])]
      (it)))

  (context "supported rendering enginges"

    (it "renders a hiccup template with extensions .hiccup.clj or .hiccup"
      (let [hiccup (render-in-layout "hiccup-template")
            hiccup-clj (render-in-layout "hiccup-template2")]
        (should= "<p>hello from hiccup!</p>" hiccup)
        (should= "<p>hello from hiccup2!</p>" hiccup-clj)))

    (it "renders a fleet template with extensions .html.fleet or .fleet"
      (let [fleet (render-in-layout "fleet-template")
            html-fleet (render-in-layout "fleet-template2")]
        (should= "<p>hello from fleet!</p>\n" fleet)
        (should= "<p>hello from fleet2!</p>\n" html-fleet)))

    (it "renders a mustache template with extensions .html.mustache or .mustache"
      (let [mustache (render-in-layout "mustache-template")
            html-mustache (render-in-layout "mustache-template2")]
        (should= "<p>hello from mustache!</p>\n" mustache)
        (should= "<p>hello from mustache2!</p>\n" html-mustache)))

    )

  (context "rendering a hiccup template inside a layout"

    (it "renders a hiccup template inside a hiccup layout"
      (let [html (render-in-layout "hiccup-template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>hello from hiccup!</p></body></html>" html)))

    (it "renders a hiccup template inside a fleet layout"
      (let [html (render-in-layout "hiccup-template" :layout "fleet-layout")]
        (should= "<html><head><title>Fleet Layout</title></head><body><p>hello from hiccup!</p></body></html>\n" html)))

    (it "renders a hiccup template inside a mustache layout"
      (let [html (render-in-layout "hiccup-template" :layout "mustache-layout")]
        (should= "<html><head><title>Mustache Layout</title></head><body><p>hello from hiccup!</p></body></html>\n" html)))

    )

  (context "rendering a hiccup partial inside a template"

    (it "renders a hiccup partial inside a hiccup template"
      (let [html (render-in-layout "hiccup-template-partial" :layout false :partial "hiccup-partial")]
        (should= "<div><p>hello from hiccup!</p><p>data-valuehello from hiccup partial!</p></div>" html)))

    (it "renders a hiccup partial inside a fleet template"
      (let [html (render-in-layout "fleet-template-partial" :layout false  :partial "hiccup-partial")]
        (should= "<div><p>hello from fleet!</p><p>data-valuehello from hiccup partial!</p></div>\n" html)))

    (it "renders a hiccup partial inside a mustache template"
      (let [partial (render "hiccup-partial" :data-key "data-value")
            html (render-in-layout "mustache-template-partial" :layout false :partial partial)]
        (should= "<div><p>hello from mustache!</p><p>data-valuehello from hiccup partial!</p></div>\n" html)))

    )

  (context "rendering a fleet template inside a layout"

    (it "renders a fleet template inside a fleet layout"
      (let [html (render-in-layout "fleet-template" :layout "fleet-layout")]
        (should= "<html><head><title>Fleet Layout</title></head><body><p>hello from fleet!</p>\n</body></html>\n" html)))

    (it "renders a fleet template inside a hiccup layout"
      (let [html (render-in-layout "fleet-template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>hello from fleet!</p>\n</body></html>" html)))

    (it "renders a fleet template inside a mustache layout"
      (let [html (render-in-layout "fleet-template" :layout "mustache-layout")]
        (should= "<html><head><title>Mustache Layout</title></head><body><p>hello from fleet!</p>\n</body></html>\n" html)))

    )

  (context "rendering a fleet partial inside a template"

    (it "renders a fleet partial inside a hiccup template"
      (let [html (render-in-layout "hiccup-template-partial" :layout false :partial "fleet-partial")]
        (should= "<div><p>hello from hiccup!</p><p>data-valuehello from fleet partial!</p>\n</div>" html)))

    (it "renders a fleet partial inside a fleet template"
      (let [html (render-in-layout "fleet-template-partial" :layout false :partial "fleet-partial")]
        (should= "<div><p>hello from fleet!</p><p>data-valuehello from fleet partial!</p>\n</div>\n" html)))

    (it "renders a fleet partial inside a mustache template"
      (let [partial (render "fleet-partial" :data-key "data-value")
            html (render-in-layout "mustache-template-partial" :layout false :partial partial)]
        (should= "<div><p>hello from mustache!</p><p>data-valuehello from fleet partial!</p>\n</div>\n" html)))

    )

  (context "rendering a mustache template inside a layout"

    (it "renders a mustache template inside a mustache layout"
      (let [html (render-in-layout "mustache-template" :layout "mustache-layout")]
        (should= "<html><head><title>Mustache Layout</title></head><body><p>hello from mustache!</p>\n</body></html>\n" html)))

    (it "renders a mustache template inside a hiccup layout"
      (let [html (render-in-layout "mustache-template" :layout "hiccup-layout")]
        (should= "<html><head><title>Hiccup Layout</title></head><body><p>hello from mustache!</p>\n</body></html>" html)))

    (it "renders a mustache template inside a fleet layout"
      (let [html (render-in-layout "mustache-template" :layout "fleet-layout")]
        (should= "<html><head><title>Fleet Layout</title></head><body><p>hello from mustache!</p>\n</body></html>\n" html)))

      )

  (context "rendering a mustache partial inside a template"

    (it "renders a mustache partial inside a hiccup template"
      (let [html (render-in-layout "hiccup-template-partial" :layout false :partial "mustache-partial")]
        (should= "<div><p>hello from hiccup!</p><p>data-valuehello from mustache partial!</p>\n</div>" html)))

    (it "renders a mustache partial inside a fleet template"
      (let [html (render-in-layout "fleet-template-partial" :layout false :partial "mustache-partial")]
        (should= "<div><p>hello from fleet!</p><p>data-valuehello from mustache partial!</p>\n</div>\n" html)))

    (it "renders a mustache partial inside a mustache template"
      (let [partial (render "mustache-partial" :data-key "data-value")
            html (render-in-layout "mustache-template-partial" :layout false :partial partial)]
        (should= "<div><p>hello from mustache!</p><p>data-valuehello from mustache partial!</p>\n</div>\n" html)))

    )

  (it "renders a template nested in a directory"
    (let [html (render-in-layout "nested/nested_template" :layout "hiccup-layout")]
      (should= "<html><head><title>Hiccup Layout</title></head><body><p>nested_template</p></body></html>" html)))

  (it "provides a nice error message when template is missing"
    (should-throw
      Exception
      "Template Not Found: non-existent\nextensions: [\"hiccup\" \"hiccup.clj\" \"html.fleet\" \"fleet\" \"html.mustache\" \"mustache\"]\nload path:  [\"spec/wrecking_ball/test_view\"]"
      (render-in-layout "non-existent")))

  )
