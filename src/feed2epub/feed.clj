; feed (RSS2, ATOM)
(ns feed2epub.feed
  (:use [clojure.contrib.duck-streams :only (reader writer)])
  (:use [clojure.xml :only (parse)]))

(defstruct feed-tags :outer :blog-title :author :entry :title :content)
(def rss2-tags (struct feed-tags :channel :title :dc:creator :item  :title :description))
(def atom-tags (struct feed-tags :feed    :title :author     :entry :title :content))
(def tagmap {:rss2 rss2-tags :atom atom-tags})


(defn get-cont [tag xml-el]
  (first
   (some #(if (= tag (:tag %)) (:content %))
          (:content xml-el))))


(defn xml-sub-seq [url tag f]
  (for [x (xml-seq (parse url)) :when (= tag (:tag x))]
    (f x)))


(defn find-cont [url tag cont-tag]
  (first (xml-sub-seq url tag #(get-cont cont-tag %))))


(defn get-feed-meta [url]
  {:blog-title (find-cont url :channel :title)
   :author     (find-cont url :channel :dc:creator)})


(defn get-feeds [url]
  (reverse
   (xml-sub-seq url :item
                #(let [title (get-cont :title %)
                       text  (get-cont :description %)]
                   (list title (str "<b>" title "</b>" text))))))