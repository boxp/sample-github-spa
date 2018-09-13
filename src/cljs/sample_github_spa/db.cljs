(ns sample-github-spa.db)

(def default-db
  {:router {:title ""
            :component (fn [] [:div "loading..."])
            :params {}}
   :repository {:repositories []
                :per-page 20
                :page 1}
   :token ""
   :api-error nil
   :history nil})
