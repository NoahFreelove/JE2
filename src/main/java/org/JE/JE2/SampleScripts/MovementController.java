package org.JE.JE2.SampleScripts;

import org.JE.JE2.IO.UserInput.Keyboard.Combos.ComboList;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.JE.JE2.Objects.Scripts.Serialize.Load;
import org.JE.JE2.Objects.Scripts.Serialize.Save;
import org.JE.JE2.Utility.Time;
import org.jbox2d.common.Vec2;

import java.util.HashMap;


public class MovementController extends Script implements Save, Load {

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
            if(Keyboard.isComboPartPressed(ComboList.LEFT) && canMoveLeft){
                physicsBody.body.setLinearVelocity(new Vec2(-moveSpeed, physicsBody.body.getLinearVelocity().y));
            }
            if(Keyboard.isComboPartPressed(ComboList.RIGHT) && canMoveRight){
                physicsBody.body.setLinearVelocity(new Vec2(moveSpeed, physicsBody.body.getLinearVelocity().y));
            }

            if(absoluteXPositioning){
                if(!Keyboard.isComboPartPressed(ComboList.RIGHT) && !Keyboard.isComboPartPressed(ComboList.LEFT)){
                    physicsBody.body.setLinearVelocity(new Vec2(0, physicsBody.body.getLinearVelocity().y));
                }
            }
            if(absoluteYPositioning){
                if(!Keyboard.isComboPartPressed(ComboList.UP )&& !Keyboard.isComboPartPressed(ComboList.DOWN)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,0));
                }
            }
            if(enableJump)
            {
                if(Keyboard.isComboPartPressed(ComboList.UP)){
                    if(physicsBody.onGround)
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,jumpVelocity));
                }
            }
            else {
                if(canMoveUp){
                    if(Keyboard.isComboPartPressed(ComboList.UP)){
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,moveSpeed));
                    }
                }
            }
            if(canMoveDown){
                if(Keyboard.isComboPartPressed(ComboList.DOWN)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,-moveSpeed));
                }
            }
        }
        else{
            if(Keyboard.isComboPartPressed(ComboList.LEFT) && canMoveLeft){
                getAttachedObject().getTransform().translateX(-moveSpeed * Time.deltaTime);
            }
            if(Keyboard.isComboPartPressed(ComboList.RIGHT) && canMoveRight){
                getAttachedObject().getTransform().translateX(moveSpeed * Time.deltaTime);
            }
            if(Keyboard.isComboPartPressed(ComboList.UP) && canMoveUp){
                getAttachedObject().getTransform().translateY(moveSpeed * Time.deltaTime);
            }
            if(Keyboard.isComboPartPressed(ComboList.DOWN) && canMoveDown){
                getAttachedObject().getTransform().translateY(-moveSpeed * Time.deltaTime);
            }
        }
    }

    @Override
    public void load(HashMap<String, String> data) {
        moveSpeed = Float.parseFloat(data.get("moveSpeed"));
        jumpVelocity = Float.parseFloat(data.get("jumpVelocity"));
        masterCanMove = Boolean.parseBoolean(data.get("masterCanMove"));
        enableJump = Boolean.parseBoolean(data.get("enableJump"));
        canMoveUp = Boolean.parseBoolean(data.get("canMoveUp"));
        canMoveDown = Boolean.parseBoolean(data.get("canMoveDown"));
        canMoveRight = Boolean.parseBoolean(data.get("canMoveRight"));
        canMoveLeft = Boolean.parseBoolean(data.get("canMoveLeft"));
        physicsBased = Boolean.parseBoolean(data.get("physicsBased"));
        absoluteXPositioning = Boolean.parseBoolean(data.get("absoluteXPositioning"));
        absoluteYPositioning = Boolean.parseBoolean(data.get("absoluteYPositioning"));

    }

    @Override
    public HashMap<String, String> save() {
        HashMap<String, String> data = new HashMap<>();
        data.put("moveSpeed", String.valueOf(moveSpeed));
        data.put("jumpVelocity", String.valueOf(jumpVelocity));
        data.put("masterCanMove", String.valueOf(masterCanMove));
        data.put("enableJump", String.valueOf(enableJump));
        data.put("canMoveUp", String.valueOf(canMoveUp));
        data.put("canMoveDown", String.valueOf(canMoveDown));
        data.put("canMoveRight", String.valueOf(canMoveRight));
        data.put("canMoveLeft", String.valueOf(canMoveLeft));
        data.put("physicsBased", String.valueOf(physicsBased));
        data.put("absoluteXPositioning", String.valueOf(absoluteXPositioning));
        data.put("absoluteYPositioning", String.valueOf(absoluteYPositioning));
        return data;
    }
}
