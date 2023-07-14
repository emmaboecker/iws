FROM gradle:jdk19 as builder
WORKDIR /usr/app
COPY . .
RUN gradle --no-daemon installBotArchive

FROM ibm-semeru-runtimes:open-20-jre-focal

WORKDIR /usr/app
COPY --from=builder /usr/app/build/installBot .

LABEL org.opencontainers.image.source = "https://github.com/StckOverflw/iws"

ENTRYPOINT ["/usr/app/bin/mikmusic"]
