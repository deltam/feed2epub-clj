(ns feed2epub.core
  (:gen-class)
  (:use [clj-epub epub])
  (:use [clojure.contrib.duck-streams :only (reader writer)])
  (:use [clojure.xml :only (parse)]))

(defn get-content-by-tag [tag xml]
  (first
   (some #(if (= tag (:tag %)) (:content %))
          (:content xml))))

(defn xml-sub-seq [url tag f]
  (for [x (xml-seq (parse url)) :when (= tag (:tag x))]
    (f x)))

(defn find-content [url tag cont-tag]
  (first (xml-sub-seq url tag #(get-content-by-tag cont-tag %))))

(defn -main [& args]
  (let [url (first args)
        blog (find-content url :channel :title)
        auther (find-content url :channel :dc:creator)
        feeds (reverse (xml-sub-seq url :item
                           #(let [title (get-content-by-tag :title %)
                                  text  (get-content-by-tag :description %)]
                              (list title (str "<b>" title "</b>" text)))))]
    (doseq [entry feeds]
      (let [title (first entry)
            text  (second entry)]
        (with-open [w (writer (str title ".txt"))]
          (.write w text))))
    (gen-epub (str blog ".epub")
              blog
              (map #(str (first %) ".txt") feeds))))
