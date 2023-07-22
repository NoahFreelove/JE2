package org.JE.JE2.Objects.Scripts.Physics.Collision;

import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Script;
import org.joml.Vector2f;

public class SimpleAABB extends Script {

    private Vector2f offset;
    private Vector2f size;

    public SimpleAABB(float x1, float x2, float y1, float y2){
        this.offset = new Vector2f(x1,y1);
        this.size = new Vector2f(x2,y2);
    }

    public SimpleAABB(Vector2f offset, Vector2f size){
        this.offset = offset;
        this.size = size;
    }

    public boolean isCollidingWith(SimpleAABB other){
        Vector2f A = getPos();
        Vector2f ASize = getSize();
        Vector2f B = other.getPos();
        Vector2f BSize = other.getSize();

        boolean inX = A.x + ASize.x >= B.x && B.x + BSize.x >= A.x;
        boolean inY = A.y + ASize.y >= B.y && B.y + BSize.y >= A.y;

        return inX && inY;
    }

    public boolean isCollidingWith(PhysicsBody physicsBody){
        Vector2f A = getPos();
        Vector2f ASize = getSize();
        Vector2f B = physicsBody.getAttachedObject().getTransform().position();
        Vector2f BSize = physicsBody.getSize();

        boolean inX = A.x + ASize.x >= B.x && B.x + BSize.x >= A.x;
        boolean inY = A.y + ASize.y >= B.y && B.y + BSize.y >= A.y;

        return inX && inY;
    }


    public Vector2f getSize(){
        return size;
    }
    private Vector2f adjustedPos = new Vector2f();
    public Vector2f getPos(){
        adjustedPos.set(getAttachedObject().getTransform().position());
        adjustedPos.x += offset.x();
        adjustedPos.y += offset.y();
        return adjustedPos;
    }
}
