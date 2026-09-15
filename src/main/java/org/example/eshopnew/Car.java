package org.example.eshopnew;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Car {

    private final Engine engine;

    public String doSth() {
        if (engine.toString().isBlank()) {
            return "No Engine";
        }
        return engine.toString();
    }
}
