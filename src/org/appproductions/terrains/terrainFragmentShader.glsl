#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(){
	
	vec4 blendMapColor=texture(blendMap, pass_textureCoords);
	vec3 unitNormal=normalize(surfaceNormal);
	vec3 unitVectortoCamera=normalize(toCameraVector);
	
	vec3 totalDiffuse=vec3(0.0);
	vec3 totalSpecular=vec3(0.0);
	
	float backTextureAmount=1-(blendMapColor.r+blendMapColor.g+blendMapColor.b);
	vec2 tiledCoords=pass_textureCoords*40.0;
	vec4 backgroundTextureColor=texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor=texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor=texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor=texture(bTexture, tiledCoords) * blendMapColor.b;
	
	vec4 totalColour=backgroundTextureColor+rTextureColor+gTextureColor+bTextureColor;
		vec3 unitLightVector=normalize(toLightVector);
		
		float nDot1=dot(unitNormal, unitLightVector);
		float brightness=max(nDot1,0.0);
		
		vec3 lightDirection=-unitLightVector;
		vec3 reflectedLightDirection=reflect(lightDirection, unitNormal);
		
		float specularFactor=dot(reflectedLightDirection, unitVectortoCamera);
		specularFactor=max(specularFactor,0.0);
		float dampedFactor=pow(specularFactor, shineDamper);
		totalDiffuse =totalDiffuse+ (brightness*lightColor);
		totalSpecular=totalSpecular+(dampedFactor*reflectivity*lightColor);
	
	totalDiffuse=max(totalDiffuse,0.2);

	out_Color=vec4(totalDiffuse,1.0)*totalColour + vec4(totalSpecular, 1.0);
	out_Color=mix(vec4(skyColor, 1.0),out_Color, visibility);
}