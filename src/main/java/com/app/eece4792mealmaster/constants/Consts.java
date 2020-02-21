package com.app.eece4792mealmaster.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Consts {
    private Consts() {
    }

    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    private static final class RecipeConsts {
        private RecipeConsts() {}
        static final String[] PATCHABLE_FIELDS = {"recipeIngredients", "instructions", "descriptions", "yield", "tags", "cookTime"};
    }

    public static final class SessionConsts {
        private SessionConsts() {}
        public static final String USER_ID = "user";
    }

    public static final class UserConsts {
        private UserConsts() {}
        static final String[] PATCHABLE_FIELDS = { "firstName", "lastName", "dob" };
    }

    private static final class StockConsts {
        private StockConsts() {}
        static final String[] PATCHABLE_FIELDS = { "location", "dateObtained", "expirationDate", "quantity" };
    }


    // Common
    public static final Map<String, String[]> PATCHABLE_FIELDS;

    static {
        Map<String, String[]> patchableFields = new HashMap<>();
        patchableFields.put("Recipe", RecipeConsts.PATCHABLE_FIELDS);
        patchableFields.put("User", UserConsts.PATCHABLE_FIELDS);
        patchableFields.put("StockItem", StockConsts.PATCHABLE_FIELDS);

        PATCHABLE_FIELDS = Collections.unmodifiableMap(patchableFields);
    }
}
