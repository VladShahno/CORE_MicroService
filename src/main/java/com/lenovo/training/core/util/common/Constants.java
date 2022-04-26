package com.lenovo.training.core.util.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static class RegularExpressions {
        public static final String SERIAL_NUMBER_REGEXP = "^[A-Za-z\\d\\-\\s]+$";
        public static final String MODEL_REGEXP = "^[A-Za-z\\d]+$";
    }

    public static final class Logging {
        public static final String DEVICE = "Device";
        public static final String SERIAL_NUMBER = "Serial number(s) ";
    }
}
