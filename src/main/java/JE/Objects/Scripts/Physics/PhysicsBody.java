package JE.Objects.Scripts.Physics;

import JE.Manager;
import JE.Objects.Scripts.Base.Script;
import JE.Utility.JOMLtoJBOX;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PhysicsBody extends Script {
    public transient org.jbox2d.dynamics.BodyDef bodyDef;
    public transient org.jbox2d.dynamics.Body body;
    public transient Fixture activeFixture;
    private boolean hasInitialized = false;
    public boolean onGround = false;
    public boolean fixedRotation = true;
    private Vector2f size = new Vector2f(1,1);
    private BodyType mode = BodyType.DYNAMIC;

    public PhysicsBody(){
        super();
    }
    
    private PhysicsBody create(BodyType defaultState, Vector2f initialPosition, Vector2f initialSize){
        bodyDef = new BodyDef();
        this.size = new Vector2f(initialSize);
        bodyDef.type = defaultState;

        Vector2f adjustedPos =  new Vector2f(initialPosition);
        adjustedPos.x += getSize().x/2;
        adjustedPos.y += getSize().y/2;
        bodyDef.position = JOMLtoJBOX.vec2(adjustedPos);
        body = Manager.activeScene().world.physicsWorld.createBody(this.bodyDef);
        body.setTransform(JOMLtoJBOX.vec2(adjustedPos), 0);

        // Set box collider
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(initialSize.x/2*1, initialSize.y/2);
        shape.setRadius(0.001f);

        activeFixture = body.createFixture(shape, 1.0f);
        activeFixture.setRestitution(0);

        hasInitialized = true;
        return this;
    }

    public void setSize(float x, float y){
        if(!hasInitialized)
            return;
        body.destroyFixture(activeFixture);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(x/2*1,y/2);
        shape.setRadius(0.001f);
        this.size = new Vector2f(x,y);
        activeFixture = body.createFixture(shape, 1.0f);
    }

    public PhysicsBody setMode(BodyType type){
        if(!hasInitialized)
        {
            mode = type;
            return this;
        }
        bodyDef.type = type;
        this.mode = type;
        body.setType(type);
        return this;
    }
    public void setFriction(float v){
        if(!hasInitialized)
            return;

        activeFixture.setFriction(v);
    }

    @Override
    public void start() {
        updateOnScriptUpdate = false;
        if(!hasInitialized()){
            create(mode, getAttachedObject().getTransform().position(), getAttachedObject().getTransform().scale());
        }
        body.setUserData(getAttachedObject());

    }

    @Override
    public void update() {
        if(!hasInitialized)
            return;

        if (body !=null)
        {
            if(mode == BodyType.STATIC)
                return;

            Vector2f pos = JOMLtoJBOX.vector2f(body.getPosition());

            Vector2f adjustedPos =  new Vector2f(pos);

            adjustedPos.x -= getSize().x /2;
            adjustedPos.y -= getSize().y/2;

            getAttachedObject().getTransform().setPosition(adjustedPos);

            if(!fixedRotation)
                getAttachedObject().getTransform().setRotation(new Vector3f(0,0, body.getAngle()));

            onGround = false;
            if(body.getType() == BodyType.DYNAMIC){
                // check if on ground directly below
                AABB aabb = new AABB();
                Vec2 pos2 = body.getPosition();
                pos2.y -= 0.5f;
                aabb.lowerBound.set(pos2);
                // Somehow this works, but I don't know why...
                pos2.y -= 0.2f;
                aabb.upperBound.set(pos2);
                Manager.activeScene().world.physicsWorld.queryAABB((fixture) -> {
                    if(fixture.getBody() != body)
                    {
                        onGround = true;
                        return true;
                    }
                    return false;
                }, aabb);
            }
        }
    }

    public Vector2f getSize() {
        return size;
    }
    public boolean hasInitialized(){return hasInitialized;}
}
