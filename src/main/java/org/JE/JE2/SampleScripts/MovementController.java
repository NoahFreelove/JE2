package org.JE.JE2.SampleScripts;

import org.JE.JE2.IO.Logging.Logger;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Utility.MethodLimiter;
import org.jbox2d.common.Vec2;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.JE.JE2.IO.UserInput.Keyboard.Combos.ComboList.*;
import static org.JE.JE2.Window.Window.deltaTime;

public class MovementController extends Script {

    public float moveSpeed = 5f;
    public float jumpVelocity = 5f;
    public boolean masterCanMove = true;
    public boolean enableJump = true;
    public boolean canMoveUp = true;
    public boolean canMoveDown = true;
    public boolean canMoveRight = true;
    public boolean canMoveLeft = true;

    public boolean physicsBased = true;
    public boolean absoluteXPositioning = true;
    public boolean absoluteYPositioning = false;

    public MovementController(){}

    private PhysicsBody physicsBody;
    @Override
    public void start(){
        physicsBody = getAttachedObject().getPhysicsBody();
    }

    @Override
    public void update() {
        if(!masterCanMove)
            return;
        if(getAttachedObject() == null)
            return;

        if(physicsBased){
            if (physicsBody == null) {
                return;
            }
            //Logger.log(getAttachedObject().getTransform().position(),2);
            if(Keyboard.isKeyPressed(65) && canMoveLeft){
                physicsBody.body.setLinearVelocity(new Vec2(-moveSpeed, physicsBody.body.getLinearVelocity().y));
            }
            if(Keyboard.isKeyPressed(68) && canMoveRight){
                physicsBody.body.setLinearVelocity(new Vec2(moveSpeed, physicsBody.body.getLinearVelocity().y));
            }

            if(absoluteXPositioning){
                if(!Keyboard.isKeyPressed(68) && !Keyboard.isKeyPressed(65)){
                    physicsBody.body.setLinearVelocity(new Vec2(0, physicsBody.body.getLinearVelocity().y));
                }
            }
            if(absoluteYPositioning){
                if(!Keyboard.isKeyPressed(87) && !Keyboard.isKeyPressed(83)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,0));
                }
            }
            if(enableJump)
            {
                if(Keyboard.isKeyPressed(87)){
                    if(physicsBody.onGround)
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,jumpVelocity));
                }
            }
            else {
                if(canMoveUp){
                    if(Keyboard.isKeyPressed(87)){
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,moveSpeed));
                    }
                }
            }
            if(canMoveDown){
                if(Keyboard.isKeyPressed(83)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,-moveSpeed));
                }
            }
        }
        else{
            if(Keyboard.isKeyPressed(65) && canMoveLeft){
                getAttachedObject().getTransform().translateX(-moveSpeed * deltaTime());
            }
            if(Keyboard.isKeyPressed(68) && canMoveRight){
                getAttachedObject().getTransform().translateX(moveSpeed * deltaTime());
            }
            if(Keyboard.isKeyPressed(87) && canMoveUp){
                getAttachedObject().getTransform().translateY(moveSpeed * deltaTime());
            }
            if(Keyboard.isKeyPressed(83) && canMoveDown){
                getAttachedObject().getTransform().translateY(-moveSpeed * deltaTime());
            }
        }
    }

}
