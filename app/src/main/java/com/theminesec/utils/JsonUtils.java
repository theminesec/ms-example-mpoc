package com.theminesec.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author eric.song
 * @since 2023/7/26 13:34
 */
public class JsonUtils {
    public static Encoder getEncoder() {
        return Encoder.INSTANCE;
    }

    public static Decoder getDecoder() {
        return Decoder.INSTANCE;
    }

    public static class Encoder {

        private Encoder() {}

        static final Encoder INSTANCE = new Encoder();

        public String encode(Object value) {
            return Impl.GSON.toJson(value);
        }

    }

    public static class Decoder {

        private Decoder() {}

        static final Decoder INSTANCE = new Decoder();

        public <T> T decode(String json, Class<T> type) {
            return Impl.GSON.fromJson(json, type);
        }

    }

    private static final class Impl {

        static final Gson GSON;

        static {
            GSON = new GsonBuilder().create();
        }

    }

    public static final class Maskable {

        public static Maskable of(String value) {
            return new Maskable(value);
        }

        private final String value;

        private Maskable(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

    }
}
