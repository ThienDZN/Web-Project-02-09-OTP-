package vn.iotstar.config;

import java.io.File;

public final class UploadConstants {
    public static final String DIR = System.getProperty("user.home")
            + File.separator + "uploads"
            + File.separator + "jpa-category-crud-assignment01";

    private UploadConstants() {
    }
}
