wallet_app
=============

暗号通貨のウォレット管理アプリケーション

## 説明
`wallet_app`は、Webページ上で暗号通貨のウォレットを作成し通貨の取引を行うことができるアプリケーションです。

※デモンストレーションであるため実際の暗号通貨は扱いません。

以下のURLにアプリケーションを公開しています。
- <https://frozen-beyond-11362.herokuapp.com>

またこのアプリケーションはブロックチェーンAPIにアクセスしています。こちらも以下に公開していますのでご参照ください。

- GitHub: <https://github.com/shungonk/sbchain>
- Heroku: <https://murmuring-depths-71832.herokuapp.com>
## 要件
- Java 11
- Maven 4.0.0
- Spring boot 2.4.2
- Bouncy Castle 1.57
- Gson 2.8.6
- PostgreSQL

## 使用方法
1. ユーザーを登録します。

1. ウォレットを作成し、詳細画面を開きます。

1. Addressに送金先アドレス、Amountに送金額を入力し、Sendボタンを押して送金します。

1. 残高が更新されていることを確認します。

1. ブロックチェーンの
[トランザクションプール](https://murmuring-depths-71832.herokuapp.com/pool)
に取引が登録されたことを確認します。

1. 数分後にブロックチェーンに
[ブロック](https://murmuring-depths-71832.herokuapp.com/chain)
が追加されていることを確認します。

1. ブロックチェーンのマイナーアカウントに報酬金が追加されていることを確認します。
    
    （マイナーアカウントはUsername:TestMiner, Password:TestMinerで公開しています。）

## ローカルでの起動
1. [ブロックチェーンAPI](https://github.com/shungonk/sbchain)
のサーバーをローカルで起動します。

1. PostgreSQLをインストールし、データベースとユーザーを作成します。

1. `src/main/resources/application.properties`にブロックチェーンサーバーの情報と
データベースの情報を設定します。
    ````properties
    # local
    spring.datasource.url=jdbc:postgresql://localhost:5432/{database}
    spring.datasource.username={username}
    spring.datasource.password={password}
    blockchain.scheme=http
    blockchain.url=localhost:{PORT}
    ````

1. リポジトリのクローンをローカルに作成します。
    ```console
    $ git clone https://github.com/shungonk/wallet_app.git
    ```

1. リポジトリのディレクトリに移動し、mavenでビルドします。
    ```console
    $ cd wallet_app
    $ mvn clean install
    ```

1. jarファイルを実行します。
    ```console
    $ java -jar target/wallet_app-0.0.1.jar
    ```

## 作者
[shungonk](https://github.com/shungonk)

## 参考情報
<https://medium.com/programmers-blockchain/blockchain-development-mega-guide-5a316e6d10df>