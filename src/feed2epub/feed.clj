; feed (RSS2, ATOM)
(ns feed2epub.feed
  (:use [clojure.contrib.duck-streams :only (reader writer)])
  (:use [clojure.xml :only (parse)]))

(defstruct feed-tags :outer :blog-title :author :entry :title :content)
(def rss2-tags (struct feed-tags :channel :title :dc:creator :item :title :description))
(def atom-tags (struct feed-tags :feed    :title :author     :entry :title :content))
(def tagmap {:rss2 rss2-tags :atom atom-tags})


(defn get-content-by-tag [tag xml]
  (first
   (some #(if (= tag (:tag %)) (:content %))
          (:content xml))))


(defn xml-sub-seq [url tag f]
  (for [x (xml-seq (parse url)) :when (= tag (:tag x))]
    (f x)))


(defn find-content [url tag cont-tag]
  (first (xml-sub-seq url tag #(get-content-by-tag cont-tag %))))




(defn feeds [url]
  (let [xs (xml-seq (parse url))
        type ((first xs) :tag)]
    (println type)
))