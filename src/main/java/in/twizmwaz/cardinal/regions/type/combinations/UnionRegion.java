package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.PointRegion;

import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 10/26/14.
 */
public class UnionRegion extends Region {

    List<Region> regions;

    public UnionRegion(String name, List<Region> regions) {
        this.regions.addAll(regions);
    }

    public UnionRegion(CombinationParser parser) {
        this.regions.addAll(parser.getRegions());
    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(BlockRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return true;
        }
        return false;
    }

    @Override
    public boolean contains(PointRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return true;
        }
        return false;
    }

    @Override
    public PointRegion getRandomPoint() {
        while (true) {
            Random random = new Random();
            PointRegion point = regions.get(random.nextInt(regions.size())).getRandomPoint();
            if (this.contains(point)) {
                return point;
            }
        }
    }
}
