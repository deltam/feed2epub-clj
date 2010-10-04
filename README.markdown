# feed2epub-clj

Convert RSS2.0/Atom feeds into .epub, on Clojure
RSS2.0/AtomフィードをePubに変換するツールをClojureで書いてみた。

ePub created by this tool, checked open it by these ePub readers.
このツールで作成したepubは、これらのePubリーダーで開けることをチェックしています。
  iBooks
  Stanza http://www.lexcycle.com/


## Usage

    $ lein deps
    $ lein uberjar 
    $ java -jar feed2epub-clj-standalone.jar "<rss_url>"

RSS2フィードのURLを文字列として指定してください。

generate <blog title>.epub
ブログタイトル.epubというファイルが出力されます。


## TODO

* debug to convert Atom feeds.
  （Atomフィード変換のバグを直す）
* added datetime on ePub metadata
* added generating datetime to ePub filename(MAYBE)
* including image file to ePub
　（画像ファイルをダウンロードしてePubに含める）
* use Tidy to clean up dirty HTML
   (http://jtidy.sourceforge.net/)

2
## License
Copyright (c) 2010 deltam (deltam@gmail.com).
Licensed under the MIT License (http://www.opensource.org/licenses/mit-license.php)
