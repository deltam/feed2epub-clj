(ns feed2epub.core
  (:gen-class)
  (:use [feed2epub feed]
        [clj-epub epub io]))


(defn entries->epub
  [blog-title author entries]
  (let [id       (str (. java.util.UUID randomUUID))
        sections (map #(first %) entries)]
    {:mimetype     (mimetype)
     :meta-inf     (meta-inf)
     :content-opf  (content-opf blog-title (or author "Nobody") id sections)
     :toc-ncx      (toc-ncx id sections)
     :html         (for [t entries]
                     (epub-text (first t) (second t)))}))


(defn -main [& args]
  (let [url (first args)
        fmeta (get-feed-meta url)
        blog   (:blog-title fmeta)
        author (:author fmeta)
        feeds (get-feeds url)]
    (println author)
    (println "entries: " (count feeds))
    (let [epub (entries->epub blog author feeds)]
      (epub->file epub (str blog "-" (java.util.Date.) ".epub")))))

