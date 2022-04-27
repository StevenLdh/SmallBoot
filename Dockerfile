FROM handday.tencentcloudcr.com/handday/erpadapter-base-maven as build
WORKDIR /app/
ADD ./ /app/
RUN mvn clean package -s /app/settings.xml -Dmaven.test.skip=true

FROM williamyeh/java8
WORKDIR /app/
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","-Xmx512m","-Xms256m","app.jar"]
