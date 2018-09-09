(ns sample-github-spa.db)

(def default-db
  {:router {:title ""
            :component (fn [] [:div "loading..."])
            :params {}}})
