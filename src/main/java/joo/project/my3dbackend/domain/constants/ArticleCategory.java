package joo.project.my3dbackend.domain.constants;

import lombok.Getter;

@Getter
public enum ArticleCategory {
    ANIMALS_PETS("동물"),
    ARCHITECTURE("건축"),
    ART_ABSTRACT("미술"),
    CARS_VEHICLES("자동차&이동수단"),
    CHARACTERS_CREATURES("캐릭터"),
    CULTURAL_HERITAGE_HISTORY("문화유산&역사"),
    ELECTRONICS_GADGETS("전기전자"),
    FASHION_STYLE("패션"),
    FOOD_DRINK("음식&음료"),
    FURNITURE_HOME("가구"),
    MUSIC("음악&악기"),
    NATURE_PLANTS("자연&식물"),
    NEWS_POLITICS("뉴스&정치"),
    PEOPLE("사람"),
    PLACES_TRAVEL("여행"),
    SCIENCE_TECHNOLOGY("과학&기술"),
    SPORTS_FITNESS("스포츠&운동"),
    WEAPONS_MILITARY("무기&군대");

    private final String description;

    ArticleCategory(String description) {
        this.description = description;
    }
}
