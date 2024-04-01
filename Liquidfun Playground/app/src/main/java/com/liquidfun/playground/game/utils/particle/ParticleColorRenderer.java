package com.liquidfun.playground.game.utils.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import finnstr.libgdx.liquidfun.ParticleSystem;

public class ParticleColorRenderer {
    protected ShaderProgram shader = createShader();
    protected Mesh mesh;

    public ParticleColorRenderer(int maxParticleNumber) {
        this.setMaxParticleNumber(maxParticleNumber);
    }

    public void setMaxParticleNumber(int count) {
        if (this.mesh != null) {
            this.mesh.dispose();
        }

        this.mesh = new Mesh(false, count, count, new VertexAttribute[]{new VertexAttribute(1, 2, "a_position"), new VertexAttribute(2, 4, "a_color")});
    }

    public int getMaxParticleNumber() {
        return this.mesh.getMaxVertices();
    }

    public void render(ParticleSystem system, float radiusScale, Matrix4 projMatrix, Float alpha) {
        Gdx.gl20.glEnable(3042);
        Gdx.gl20.glBlendFunc(770, 771);
        Gdx.gl20.glEnable(34370);
        Gdx.gl20.glEnable(34913);
        this.shader.begin();
        this.shader.setUniformf("alpha", alpha);
        this.shader.setUniformf("particlesize", system.getParticleRadius());
        this.shader.setUniformf("scale", radiusScale);
        this.shader.setUniformMatrix("u_projTrans", projMatrix);
        this.mesh.setVertices(system.getParticlePositionAndColorBufferArray(true));
        this.mesh.render(this.shader, 0, 0, system.getParticleCount());
        this.shader.end();
        Gdx.gl20.glDisable(34913);
    }

    public void dispose() {
        this.shader.dispose();
        this.mesh.dispose();
    }

    public static final ShaderProgram createShader() {
        String prefix = "";
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            prefix = prefix + "#version 120\n";
        } else {
            prefix = prefix + "#version 100\n";
        }

        String vertexShader = "attribute vec4 a_position;\n\nuniform float particlesize;\nuniform float scale;\nuniform mat4 u_projTrans;\n\nattribute vec4 a_color;\nvarying vec4 v_color;\n\nvoid main()\n{\n   gl_Position =  u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n   gl_PointSize = scale * particlesize;\n   v_color = a_color;}\n";
        //String fragmentShader = "#ifdef GL_ES\n#define LOWP lowp\nprecision mediump float;\n#else\n#define LOWP \n#endif\nvarying vec4 v_color;\nvoid main()\n{\n float len = length(vec2(gl_PointCoord.x - 0.5, gl_PointCoord.y - 0.5));\n if(len <= 0.5) {\n \tgl_FragColor = v_color;\n } else {\n \tgl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n }\n}";
        String fragmentShader = "#ifdef GL_ES\n#define LOWP lowp\nprecision mediump float;\n#else\n#define LOWP \n#endif\nvarying vec4 v_color;\nuniform float alpha;\nvoid main()\n{\n float len = length(vec2(gl_PointCoord.x - 0.5, gl_PointCoord.y - 0.5));\n if(len <= 0.5) {\n \tgl_FragColor = v_color * alpha; // Використовуйте параметр прозорості\n } else {\n \tgl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n }\n}";

        ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.log("ERROR", shader.getLog());
        }

        return shader;
    }
}
