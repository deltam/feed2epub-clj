(ns feed2epub.core
  (:gen-class)
  (:use [feed2epub feed])
  (:use [clj-epub epub])
  (:use [clojure.contrib.duck-streams :only (reader writer)])
  (:use [clojure.xml :only (parse)]))


(defn -main [& args]
  (let [url (first args)
        fmeta (get-feed-meta url)
        blog   (:blog-title fmeta)
        author (:author fmeta)
        feeds (get-feeds url)]
    (doseq [entry feeds]
      (let [title (first entry)
            text  (second entry)]
        (with-open [w (writer (str title ".txt"))]
          (.write w text))))
    (println author)
    (gen-epub (str blog ".epub")
              blog
              (map #(str (first %) ".txt") feeds))))
