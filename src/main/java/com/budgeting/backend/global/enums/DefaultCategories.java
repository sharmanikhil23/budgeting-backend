package com.budgeting.backend.global.enums;

import java.util.List;
import java.util.Map;

public class DefaultCategories {

    // Expense categories: parent -> children
    public static final Map<String, List<CategoryWithIcon>> EXPENSES = Map.of(
            "Food", List.of(
                    new CategoryWithIcon("Groceries", "🛒"),
                    new CategoryWithIcon("Restaurants", "🍽️"),
                    new CategoryWithIcon("Coffee", "☕")
            ),
            "Transport", List.of(
                    new CategoryWithIcon("Gas", "⛽"),
                    new CategoryWithIcon("Transit", "🚌"),
                    new CategoryWithIcon("Parking", "🅿️"),
                    new CategoryWithIcon("Insurance", "🛡️")
            ),
            "Utilities", List.of(
                    new CategoryWithIcon("Electricity", "💡"),
                    new CategoryWithIcon("Internet", "🌐"),
                    new CategoryWithIcon("Mobile", "📱")
            ),
            "Entertainment", List.of(
                    new CategoryWithIcon("Movies", "🎬"),
                    new CategoryWithIcon("Subscriptions", "📺")
            ),
            "Housing", List.of(
                    new CategoryWithIcon("Rent", "🏠"),
                    new CategoryWithIcon("Tenant Insurance", "🛡️")
            )
    );

    // Income categories
    public static final List<CategoryWithIcon> INCOME = List.of(
            new CategoryWithIcon("Salary", "💰"),
            new CategoryWithIcon("Bonus", "🎁"),
            new CategoryWithIcon("Investment", "📈")
    );

    // Savings categories
    public static final List<CategoryWithIcon> SAVINGS = List.of(
            new CategoryWithIcon("Saving Account", "🏦")
    );

    // Category type enum
    public static enum CategoryType {
        EXPENSE,
        INCOME,
        SAVING
    }

    // Helper class for name + icon
    public static class CategoryWithIcon {
        private final String name;
        private final String icon;

        public CategoryWithIcon(String name, String icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }
    }
}
