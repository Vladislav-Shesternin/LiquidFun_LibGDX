#Box2d
#-keepclassmembers class com.badlogic.gdx.physics.box2d.World {
#   boolean contactFilter(long, long);
#   void    beginContact(long);
#   void    endContact(long);
#   void    preSolve(long, long);
#   void    postSolve(long, long);
#   boolean reportFixture(long);
#   float   reportRayFixture(long, float, float, float, float, float);
#   # LiquidFun
#   void    beginParticleBodyContact(long, long);
#   void    endParticleBodyContact(long, long, int);
#   void    beginParticleContact(long, long);
#   void    endParticleContact(long, int, int);
#   boolean reportParticle(long, int);
#   boolean shouldQueryParticleSystem(long);
#   float   reportRayParticle(long, int, float, float, float, float, float);
#   boolean rayShouldQueryParticleSystem(long);
#}

-keepclassmembers class com.badlogic.gdx.physics.box2d.World { *; }
-dontwarn javax.annotation.Nullable