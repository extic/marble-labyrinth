/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.extremely.engine.rendering.loading;

public class ObjIndex {
    private int vertexIndex;
    private int texCoordIndex;
    private int normalIndex;


    public int getVertexIndex() {
        return vertexIndex;
    }

    public void setVertexIndex(int vertexIndex) {
        this.vertexIndex = vertexIndex;
    }

    public int getTexCoordIndex() {
        return texCoordIndex;
    }

    public void setTexCoordIndex(int texCoordIndex) {
        this.texCoordIndex = texCoordIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjIndex objIndex = (ObjIndex) o;

        if (vertexIndex != objIndex.vertexIndex) return false;
        if (texCoordIndex != objIndex.texCoordIndex) return false;
        return normalIndex == objIndex.normalIndex;
    }

    @Override
    public int hashCode() {
        int result = vertexIndex;
        result = 31 * result + texCoordIndex;
        result = 31 * result + normalIndex;
        return result;
    }
}
