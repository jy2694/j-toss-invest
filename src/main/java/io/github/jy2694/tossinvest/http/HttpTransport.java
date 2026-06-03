package io.github.jy2694.tossinvest.http;

/** Sends a fully-built request over the wire. Pluggable for testing. */
public interface HttpTransport {
    TossResponse send(TossRequest request);
}
