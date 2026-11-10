package com.roxideal.backend.finish;

public record FinishDto(Long id, String slug, String name, String swatch) {

    public static FinishDto from(Finish finish) {
        return new FinishDto(finish.getId(), finish.getSlug(), finish.getName(), finish.getSwatch());
    }
}
