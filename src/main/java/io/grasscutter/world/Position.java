package io.grasscutter.world;

import com.github.davidmoten.rtreemulti.geometry.Point;
import com.google.gson.annotations.SerializedName;
import io.grasscutter.proto.VectorOuterClass.Vector;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.interfaces.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Position implements Serializable, Cloneable {
    @SerializedName(
            value = "x",
            alternate = {"_x", "X"})
    @Getter
    @Setter
    private float x;

    @SerializedName(
            value = "y",
            alternate = {"_y", "Y"})
    @Getter
    @Setter
    private float y;

    @SerializedName(
            value = "z",
            alternate = {"_z", "Z"})
    @Getter
    @Setter
    private float z;

    public Position() {}

    /**
     * Creates a new position.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(float x, float y) {
        this.set(x, y);
    }

    /**
     * Creates a new position.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public Position(float x, float y, float z) {
        this.set(x, y, z);
    }

    /**
     * Parses a position from a list of floats in the format [x, y, z]
     *
     * @param xyz The list to parse
     */
    public Position(List<Float> xyz) {
        switch (xyz.size()) {
            default: // Might want to error on excess elements, but maybe we want to extend to 3+3
                // representation later.
            case 3:
                this.z = xyz.get(2); // Fall-through
            case 2:
            case 1:
                this.y = xyz.get(0); // pointless fall-through
            case 0:
                break;
        }
    }

    /**
     * Parses a position from a string in the format "x,y,z"
     *
     * @param position The string to parse
     */
    public Position(String position) {
        var split = position.split(",");

        if (split.length >= 2) {
            this.x = Float.parseFloat(split[0]);
            this.y = Float.parseFloat(split[1]);
        }

        if (split.length >= 3) {
            this.z = Float.parseFloat(split[2]);
        }
    }

    public Position(Vector vector) {
        this.set(vector);
    }

    public Position(Position pos) {
        this.set(pos);
    }

    public Position set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the position to the provided position. Performs a deep copy.
     *
     * @param pos The position to copy.
     * @return The position.
     */
    public Position set(Position pos) {
        return this.set(pos.getX(), pos.getY(), pos.getZ());
    }

    public Position set(Vector pos) {
        return this.set(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Sets the position to the provided position.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The position.
     */
    public Position set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Adds the coordinates of the provided position to this position.
     *
     * @param add The position to add.
     * @return The position.
     */
    public Position add(Position add) {
        this.x += add.getX();
        this.y += add.getY();
        this.z += add.getZ();
        return this;
    }

    public Position addX(float d) {
        this.x += d;
        return this;
    }

    public Position addY(float d) {
        this.y += d;
        return this;
    }

    public Position addZ(float d) {
        this.z += d;
        return this;
    }

    public Position subtract(Position sub) {
        this.x -= sub.getX();
        this.y -= sub.getY();
        this.z -= sub.getZ();
        return this;
    }

    /**
     * Performs a translation on the position. The angle is in radians by default.
     *
     * @param convert Should the angle be converted to degrees?
     * @param dist The distance to translate.
     * @param angle The angle to translate.
     * @return The position.
     */
    public Position translate(boolean convert, float dist, float angle) {
        if (convert) angle = (float) Math.toRadians(angle);

        this.x += dist * Math.sin(angle);
        this.y += (convert ? -dist : dist) * Math.cos(angle);
        return this;
    }

    /**
     * Performs a 2D comparison.
     *
     * @param other The position to compare to.
     * @return True if the positions are equal.
     */
    public boolean equal2d(Position other) {
        return this.getX() == other.getX() && this.getZ() == other.getZ();
    }

    /**
     * Performs a 3D comparison.
     *
     * @param other The position to compare to.
     * @return True if the positions are equal.
     */
    public boolean equal3d(Position other) {
        return this.getX() == other.getX()
                && this.getY() == other.getY()
                && this.getZ() == other.getZ();
    }

    /**
     * Calculates the distance between two points.
     *
     * @param other The position to compare to.
     * @return The distance between the two points.
     */
    public double distance(Position other) {
        var detX = this.getX() - other.getX();
        var detY = this.getY() - other.getY();
        var detZ = this.getZ() - other.getZ();

        return Math.sqrt(detX * detX + detY * detY + detZ * detZ);
    }

    /**
     * Creates a new position of a nearby location. 2 dimensional.
     *
     * @param range The range to generate a position in.
     * @return The position.
     */
    public Position nearby2d(float range) {
        var position = this.clone();
        position.z += CryptoUtils.randomNumber(-range, range);
        position.x += CryptoUtils.randomNumber(-range, range);
        return position;
    }

    @Override
    public Position clone() {
        return new Position(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    /**
     * Converts the position to a game vector.
     *
     * @return A game vector. (instance of {@link Vector})
     */
    public Vector toProto() {
        return Vector.newBuilder().setX(this.getX()).setY(this.getY()).setZ(this.getZ()).build();
    }

    public Point toPoint() {
        return Point.create(x, y, z);
    }

    /** To XYZ array for Spatial Index */
    public double[] toDoubleArray() {
        return new double[] {x, y, z};
    }

    /** To XZ array for Spatial Index (Blocks) */
    public double[] toXZDoubleArray() {
        return new double[] {x, z};
    }
}
