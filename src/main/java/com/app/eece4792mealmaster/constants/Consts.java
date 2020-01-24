package com.app.eece4792mealmaster.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Consts {
    private Consts() {
    }

    private static final class RecipeConsts {
        private RecipeConsts() {}
        static final String[] PATCHABLE_FIELDS = {"ingredients", "instructions", "descriptions", "yield", "tags"};
    }

    public static final class SessionConsts {
        private SessionConsts() {}
        public static final String USER_ID = "user";
    }


    // Common
    public static final Map<String, String[]> PATCHABLE_FIELDS;

    static {
        Map<String, String[]> patchableFields = new HashMap<>();
        patchableFields.put("Recipe", RecipeConsts.PATCHABLE_FIELDS);

        PATCHABLE_FIELDS = Collections.unmodifiableMap(patchableFields);
    }
}
