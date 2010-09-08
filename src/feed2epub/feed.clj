; feed (RSS2, ATOM)
(ns feed2epub.feed
  (:use [clojure.contrib.duck-streams :only (reader writer)])
  (:use [clojure.xml :only (parse)]))

;(defstruct feed-tags :outer :blog-title :author :entry :title :content)
;(def rss2-tags (struct feed-tags :channel :title :dc:creator :item  :title :description))
;(def atom-tags (struct feed-tags :feed    :title :author     :entry :title :content))
;(def tagmap {:rss2 rss2-tags :atom atom-tags})


(defn get-cont
  "xml-el要素内の最初に出現するtagタグの内容を返す"
  [tag xml-el]
  (first
   (some #(if (= tag (:tag %)) (:content %))
          (:content xml-el))))


(defn xml-sub-seq
  "urlで指定したXMLを取得し、指定されたtagタグで絞り込み、要素を関数fで変換したシーケンスを返す"
  [url tag f]
  (for [x (xml-seq (parse url)) :when (= tag (:tag x))]
    (f x)))


(defn find-cont
  "urlから取得したXMLからtagの内側のcont-tagタグの内容を返す"
  [url tag cont-tag]
  (first (xml-sub-seq url tag #(get-cont cont-tag %))))


(defn- ret-tags
  "XMLシーケンスを受け取り、RSS2/Atomを判別して、それぞれで情報取得に必要なタグのMapを返す"
  [xseq]
  (let [type ((first xseq) :tag)]
    (condp = type
      :rss  {:outer :channel, :blog-title :title, :author :dc:creator,
             :entry :item,  :title :title, :text :description}
      :feed {:outer :feed   , :blog-title :title, :author :author,
             :entry :entry, :title :title, :text :content})))


(defn get-feed-meta
  "urlにしてされたフィードXMLのメタデータをMapで返す（ブログ名、著者）"
  [url]
  (let [xs (xml-seq (parse url))
        tags (ret-tags xs)]
    {:blog-title (find-cont url (tags :outer) (tags :blog-title))
     :author     (find-cont url (tags :outer) (tags :author))}))


(defn get-feeds
  "urlで指定されたフィードXMLからタイトルと内容のリストを作り返す。記事の順序は旧->新の順にする"
  [url]
  (let [xs (xml-seq (parse url))
        tags (ret-tags xs)]
    (reverse
     (xml-sub-seq url
                  (tags :entry)
                  #(let [title (get-cont (tags :title) %)
                         text  (get-cont (tags :text) %)]
                     (list title (str "<b>" title "</b>" text)))))))