package com.mygdx.game.Logic.Physics;

import com.mygdx.game.Logic.GameObjects.GameObj;
import java.util.ArrayList;

public interface CollisionManager {
    void impactReact();

    boolean impactReaction(boolean z);

    boolean isCollision();

    void manageCollisions();

    void setPosColl(ArrayList<GameObj> arrayList);
}
