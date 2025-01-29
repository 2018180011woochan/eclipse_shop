package com.example.shop_project_v2.member;

public enum Membership {
	BRONZE("BRONZE", 0),
    SILVER("SILVER", 200000),
    GOLD("GOLD", 500000),
    DIAMOND("DIAMOND", 1000000);

    private final String name;
    private final int requiredPoints;

    Membership(String name, int requiredPoints) {
        this.name = name;
        this.requiredPoints = requiredPoints;
    }

    public String getName() {
        return name;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }
    
    // 소문자로 변환하는 메서드 추가
    public String toLowerCase() {
        return this.name().toLowerCase();
    }

    // CSS 클래스를 가져오는 메서드 추가
    public String getCssClass() {
        return "membership-" + this.toLowerCase();
    }
}
