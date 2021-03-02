# 사전 준비
### H2 설치 : http://h2database.com/html/main.html
* sql 폴더에 category_insert_20200407.sql, product_insert_20200407.sql 실행
* H2 디비에서 product_insert.sql 과 category_insert.sql 실행 후
DB 시퀀스를 생성하였습니다.

* CREATE SEQUENCE PRODUCT_SEQ START WITH 1001
INCREMENT BY 1;
* CREATE SEQUENCE CATEGORY_SEQ START WITH 11
INCREMENT BY 1;

###IntelliJ와 Java 11 버전 사용하였습니다.

# Swagger UI
* 아래 스웨거에서 기본적인 CRUD 및 캐시 작동을 테스트 합니다.
http://127.0.0.1:8080/swagger-ui/

# 타이머 캐시 적용
* 조회 후 10분이 지나면 다시 조회 후 캐시
1. 전체 카테고리 조회
2. 카테고리별 상품 리스트 조회

# LRU 캐시 적용
* 캐시 사이즈는 테스트를 위해 4로 지정
1. 상품 조회

ex) 1,2,3,4번 조회 시점마다 DB 조회 후 캐시됨.
5번 조회 시 1번이 cache 에서 evict 되고 cache에는 5,4,3,2 가 저장됨.
1번은 evict 된 상태이므로 1번 조회 시 DB 조회 후 캐시됨.
다시 1번 조회 시 캐시된 값 리턴

# LRU 캐시 최적화
* 상품 조회를 로깅하여 가장 많이 조회되는 상품들의 수를 적정 캐시 사이즈로 정한다.
* 어드민 등에서 데이터 변경 시 캐시 클리어 API 제공