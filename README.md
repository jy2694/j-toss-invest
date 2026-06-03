# j-toss-invest

토스증권 Open API 비공식 Java 클라이언트 라이브러리입니다.

> [!IMPORTANT]
> 본 클라이언트 라이브러리는 토스증권 Open API가 사전예약 상태임에 따라 실호출 테스트가 되지 않은 상태입니다.

## 요구 환경

- Java 17+
- 의존성: Jackson (`jackson-databind`, `jackson-datatype-jsr310`) — 자동으로 함께 받아집니다.

## 설치

### Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.jy2694:j-toss-invest:0.1.0'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.jy2694:j-toss-invest:0.1.0")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.jy2694</groupId>
    <artifactId>j-toss-invest</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 빠른 시작

```java
import io.github.jy2694.tossinvest.client.TossInvestClient;
import io.github.jy2694.tossinvest.model.account.Account;

import java.util.List;

TossInvestClient client = TossInvestClient.create("your_client_id", "your_client_secret");
client.authenticate(); // 토큰 발급 후 클라이언트에 저장됩니다.

List<Account> accounts = client.getAccounts();
long accountSeq = accounts.get(0).accountSeq();
```

`TossInvestClient` 는 모든 기능 API(`AuthenticationApi`, `MarketDataApi`, `AccountApi`, `OrderApi`)를
구현하므로, 인증 후에는 한 객체로 전체 엔드포인트를 호출합니다. 발급한 토큰은 인증이 필요한 요청에
`Authorization: Bearer` 헤더로 자동 첨부됩니다.

---

## API 레퍼런스

모든 금액·수량 필드는 `BigDecimal`, 날짜시간은 `OffsetDateTime`, 날짜는 `LocalDate` 로 매핑됩니다.
응답 DTO 는 record 이므로 필드 접근은 `price()` 처럼 메서드 호출입니다.

### 인증

```java
import io.github.jy2694.tossinvest.model.auth.OAuth2TokenResponse;

OAuth2TokenResponse token = client.authenticate();
token.accessToken();  // 액세스 토큰
token.tokenType();    // "Bearer"
token.expiresIn();    // 만료 시간(초)

// 토큰 만료 시 동일 메서드로 재발급
client.authenticate();

// 외부에서 발급한 토큰을 직접 주입할 수도 있습니다.
client.setAccessToken("existing-token");
```

### Market Data

| 메서드 | 설명 |
|--------|------|
| `getOrderbook(symbol)` | 호가 조회 |
| `getPrices(symbols)` | 현재가 조회 (콤마 구분, 최대 200개) |
| `getTrades(TradeQuery)` | 최근 체결 내역 조회 |
| `getPriceLimit(symbol)` | 상/하한가 조회 |
| `getCandles(CandleQuery)` | 캔들 차트 조회 |

```java
import io.github.jy2694.tossinvest.model.market.*;
import io.github.jy2694.tossinvest.model.query.CandleQuery;
import io.github.jy2694.tossinvest.model.query.TradeQuery;

// 호가
OrderbookResponse orderbook = client.getOrderbook("005930");
for (OrderbookEntry ask : orderbook.asks()) {
    System.out.printf("매도 %s x %s%n", ask.price(), ask.volume());
}

// 현재가 (다건, 콤마 구분 문자열)
for (PriceResponse p : client.getPrices("005930,AAPL")) {
    System.out.printf("%s: %s %s%n", p.symbol(), p.lastPrice(), p.currency());
}

// 최근 체결
List<Trade> trades = client.getTrades(TradeQuery.of("005930").count(10).build());
for (Trade t : trades) {
    System.out.printf("%s: %s x %s%n", t.timestamp(), t.price(), t.volume());
}

// 상/하한가
PriceLimitResponse limits = client.getPriceLimit("005930");
System.out.printf("상한 %s, 하한 %s%n", limits.upperLimitPrice(), limits.lowerLimitPrice());

// 캔들 (ONE_DAY 일봉, ONE_MINUTE 분봉)
CandlePageResponse page = client.getCandles(
        CandleQuery.of("005930", CandleInterval.ONE_DAY).count(20).build());
for (Candle c : page.candles()) {
    System.out.printf("%s: O=%s H=%s L=%s C=%s%n",
            c.timestamp(), c.openPrice(), c.highPrice(), c.lowPrice(), c.closePrice());
}

// 다음 페이지 (커서)
if (page.nextBefore() != null) {
    CandlePageResponse next = client.getCandles(
            CandleQuery.of("005930", CandleInterval.ONE_DAY)
                    .before(page.nextBefore().toString())
                    .build());
}
```

### Market Info

| 메서드 | 설명 |
|--------|------|
| `getExchangeRate(ExchangeRateQuery)` | 환율 조회 |
| `getKrMarketCalendar(date)` | 국내 장 운영 시간 조회 |
| `getUsMarketCalendar(date)` | 미국 장 운영 시간 조회 |

```java
import io.github.jy2694.tossinvest.model.common.Currency;
import io.github.jy2694.tossinvest.model.market.*;
import io.github.jy2694.tossinvest.model.query.ExchangeRateQuery;

// 환율 (1분 주기 갱신, 참고용)
ExchangeRateResponse rate = client.getExchangeRate(
        ExchangeRateQuery.of(Currency.USD, Currency.KRW).build());
System.out.printf("USD/KRW: %s (유효: %s ~ %s)%n",
        rate.rate(), rate.validFrom(), rate.validUntil());

// 국내 장 운영 시간 (date 에 null 을 주면 오늘 기준)
KrMarketCalendarResponse krCal = client.getKrMarketCalendar(null);
KrMarketDay krToday = krCal.today();
if (krToday.integrated() != null && krToday.integrated().regularMarket() != null) {
    RegularMarketSession reg = krToday.integrated().regularMarket();
    System.out.printf("정규장: %s ~ %s%n", reg.startTime(), reg.endTime());
}

// 미국 장 운영 시간
UsMarketCalendarResponse usCal = client.getUsMarketCalendar(null);
UsMarketDay usToday = usCal.today();
if (usToday.regularMarket() != null) {
    System.out.printf("정규장: %s ~ %s%n",
            usToday.regularMarket().startTime(), usToday.regularMarket().endTime());
}
```

### Stock Info

| 메서드 | 설명 |
|--------|------|
| `getStocks(symbols)` | 종목 기본 정보 조회 (콤마 구분, 최대 200개) |
| `getStockWarnings(symbol)` | 매수 유의사항 조회 |

```java
import io.github.jy2694.tossinvest.model.market.StockInfo;
import io.github.jy2694.tossinvest.model.market.StockWarning;

// 종목 기본 정보
for (StockInfo s : client.getStocks("005930,AAPL")) {
    System.out.printf("%s %s (%s) - %s%n", s.symbol(), s.name(), s.market(), s.status());
    if (s.koreanMarketDetail() != null) {
        System.out.printf("  KRX 거래정지: %s%n", s.koreanMarketDetail().krxTradingSuspended());
    }
}

// 매수 유의사항 (활성 경고만 반환, 없으면 빈 리스트)
for (StockWarning w : client.getStockWarnings("005930")) {
    System.out.printf("%s (%s): %s ~ %s%n",
            w.warningType(), w.exchange(), w.startDate(), w.endDate());
}
```

### Account & Asset

| 메서드 | 설명 |
|--------|------|
| `getAccounts()` | 계좌 목록 조회 |
| `getHoldings(accountSeq, symbol)` | 보유 주식 조회 |

```java
import io.github.jy2694.tossinvest.model.account.*;

List<Account> accounts = client.getAccounts();
for (Account acc : accounts) {
    System.out.printf("계좌번호: %s, seq: %d, 유형: %s%n",
            acc.accountNo(), acc.accountSeq(), acc.accountType());
}
long accountSeq = accounts.get(0).accountSeq();

// 전체 보유 종목 (symbol 에 null)
HoldingsOverview holdings = client.getHoldings(accountSeq, null);
System.out.printf("KRW 투자원금: %s%n", holdings.totalPurchaseAmount().krw());
System.out.printf("손익률: %s%n", holdings.profitLoss().rate());
for (HoldingsItem item : holdings.items()) {
    System.out.printf("%s %s: %s주, 현재가 %s %s%n",
            item.symbol(), item.name(), item.quantity(), item.lastPrice(), item.currency());
    System.out.printf("  손익: %s (%s)%n", item.profitLoss().amount(), item.profitLoss().rate());
}

// 특정 종목만 필터링
HoldingsOverview filtered = client.getHoldings(accountSeq, "005930");
```

### Order

| 메서드 | 설명 |
|--------|------|
| `createOrder(accountSeq, OrderCreateRequest)` | 주문 생성 |
| `modifyOrder(accountSeq, orderId, OrderModifyRequest)` | 주문 정정 |
| `cancelOrder(accountSeq, orderId)` | 주문 취소 |

주문 객체는 `OrderCreateRequest` 의 팩토리(`limit` / `market` / `amountBased`)로 만들고,
`clientOrderId`·`timeInForce` 같은 선택 항목은 `toBuilder()` 로 채웁니다.

```java
import io.github.jy2694.tossinvest.model.order.*;
import java.math.BigDecimal;

// 국내 지정가 매수
OrderResponse result = client.createOrder(accountSeq,
        OrderCreateRequest.limit("005930", OrderSide.BUY,
                new BigDecimal("10"), new BigDecimal("70000")));
String orderId = result.orderId();

// 미국 시장가 매수 (금액 기준, 정규장만 가능)
client.createOrder(accountSeq,
        OrderCreateRequest.amountBased("AAPL", OrderSide.BUY, new BigDecimal("100.00")));

// 미국 LOC 주문 (LIMIT + CLS), 멱등성 키 지정
client.createOrder(accountSeq,
        OrderCreateRequest.limit("AAPL", OrderSide.BUY,
                        new BigDecimal("5"), new BigDecimal("185.50"))
                .toBuilder()
                .timeInForce(TimeInForce.CLS)
                .clientOrderId("my-order-001")
                .build());

// 주문 정정 (국내: 가격+수량, 미국: 가격만)
client.modifyOrder(accountSeq, orderId,
        OrderModifyRequest.of(OrderType.LIMIT)
                .price(new BigDecimal("71000"))
                .quantity(new BigDecimal("15"))
                .build());

// 주문 취소
client.cancelOrder(accountSeq, orderId);
```

### Order History

| 메서드 | 설명 |
|--------|------|
| `getOrders(accountSeq, OrderQuery)` | 주문 목록 조회 |
| `getOrder(accountSeq, orderId)` | 주문 상세 조회 |

```java
import io.github.jy2694.tossinvest.model.order.*;
import io.github.jy2694.tossinvest.model.query.OrderQuery;
import io.github.jy2694.tossinvest.model.query.OrderStatusFilter;

// 진행 중 주문 목록
PaginatedOrderResponse orders = client.getOrders(accountSeq,
        OrderQuery.of(OrderStatusFilter.OPEN).build());
for (Order order : orders.orders()) {
    System.out.printf("%s: %s %s %s%n",
            order.orderId(), order.symbol(), order.side(), order.status());
    System.out.printf("  체결수량: %s%n", order.execution().filledQuantity());
}

// 특정 종목 필터링
client.getOrders(accountSeq,
        OrderQuery.of(OrderStatusFilter.OPEN).symbol("005930").build());

// 주문 상세
Order order = client.getOrder(accountSeq, orderId);
System.out.printf("주문상태: %s, 체결가: %s%n",
        order.status(), order.execution().averageFilledPrice());
```

### Order Info

| 메서드 | 설명 |
|--------|------|
| `getBuyingPower(accountSeq, currency)` | 매수 가능 금액 조회 |
| `getSellableQuantity(accountSeq, symbol)` | 판매 가능 수량 조회 |
| `getCommissions(accountSeq)` | 매매 수수료 조회 |

```java
import io.github.jy2694.tossinvest.model.account.*;
import io.github.jy2694.tossinvest.model.common.Currency;

// 매수 가능 금액
BuyingPowerResponse bp = client.getBuyingPower(accountSeq, Currency.KRW);
System.out.printf("매수 가능: %s %s%n", bp.cashBuyingPower(), bp.currency());

// 판매 가능 수량
SellableQuantityResponse sq = client.getSellableQuantity(accountSeq, "005930");
System.out.printf("판매 가능: %s주%n", sq.sellableQuantity());

// 수수료율
for (Commission c : client.getCommissions(accountSeq)) {
    System.out.printf("%s: %s (%s ~ %s)%n",
            c.marketCountry(), c.commissionRate(), c.startDate(), c.endDate());
}
```

---

## 에러 처리

모든 예외는 unchecked(`RuntimeException` 계열)이므로 `try/catch` 가 강제되지 않습니다.
비-2xx 응답은 `TossApiStatusException` (HTTP 상태·코드·`requestId` 포함)으로 던져지며,
자주 분기하는 케이스에는 전용 하위 예외가 있습니다.

| 예외 | 상황 |
|------|------|
| `TossApiException` | 모든 예외의 최상위 (직렬화·통신 오류 포함) |
| `TossApiStatusException` | 비-2xx 응답 (상태·코드·`requestId` 보유) |
| `AuthenticationException` | HTTP 401 (인증/인가 실패) |
| `RateLimitException` | HTTP 429 (호출 한도 초과) |
| `InvalidRequestException` | HTTP 400 / 422 (요청 검증 실패) |

```java
import io.github.jy2694.tossinvest.exception.*;

try {
    client.authenticate();
} catch (AuthenticationException e) {
    System.out.println("잘못된 client_id / client_secret");
}

try {
    List<Account> accounts = client.getAccounts();
} catch (AuthenticationException e) {
    client.authenticate(); // 토큰 재발급 후 재시도
    client.getAccounts();
} catch (RateLimitException e) {
    System.out.println("Rate limit 초과: " + e.getMessage());
} catch (TossApiStatusException e) {
    System.out.printf("API 오류 [%s] (status=%d, requestId=%s): %s%n",
            e.getCode(), e.getStatusCode(), e.getRequestId(), e.getMessage());
} catch (TossApiException e) {
    System.out.println("통신/직렬화 오류: " + e.getMessage());
}
```

---

## 라이선스

MIT License
