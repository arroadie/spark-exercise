.PHONY: depend clean start install

start:
	docker-compose up -d && \
	docker exec -it sparkmaster spark-submit --class io.thiagocosta.stockaverage.StockAverage --master local --deploy-mode client /stockaverage/target/scala-2.12/StockAverage-assembly-0.0.1.jar
install:
	docker-compose build

util:
	docker-compose run util

clean:
	docker container prune

# DO NOT DELETE THIS LINE -- make depend needs it
