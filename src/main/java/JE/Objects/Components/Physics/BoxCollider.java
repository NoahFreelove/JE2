package JE.Objects.Components.Physics;

import JE.Manager;
import JE.Objects.Components.Base.Component;
import JE.Utility.JOMLtoJBOX;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class BoxCollider extends Component {
    public Fixture collider;
    public Body body;
    private Vector2f size;
    private boolean hasInitialized = false;
    public BoxCollider(){}

    public BoxCollider create(Vector2f initialPos, Vector2f size){
        this.size = size;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x/2*1, size.y/2);
        shape.setRadius(0.001f);
        BodyDef def = new BodyDef();
        def.type = BodyType.STATIC;
        def.position = JOMLtoJBOX.vec2(initialPos);

        body = Manager.activeScene().world.physicsWorld.createBody(def);
        body.setTransform(JOMLtoJBOX.vec2(initialPos), 0);

        collider = body.createFixture(shape, 1);
        hasInitialized = true;
        return this;
    }

    @Override
    public void update() {
        if(!hasInitialized)
            return;
        if(parentObject() == null)
            return;
        Vector2f pos = new Vector2f(parentObject().getTransform().position());
        pos.x+= size.x()/2;
        pos.y+= size.y()/2;
        body.setTransform(JOMLtoJBOX.vec2(pos),0);
    }
}
