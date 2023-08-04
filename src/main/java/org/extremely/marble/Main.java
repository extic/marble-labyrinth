package org.extremely.marble;

import org.extremely.engine.core.Engine;

public class Main {
    public static void main(String[] args) {
        var engine = Engine.getInstance();
        engine.init(new MarbleGame());
        engine.start();
    }
}
