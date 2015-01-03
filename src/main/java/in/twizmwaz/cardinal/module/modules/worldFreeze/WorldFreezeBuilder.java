package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;

import java.util.ArrayList;
import java.util.List;

public class WorldFreezeBuilder implements ModuleBuilder {
    
    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        results.add(new WorldFreeze(match));
        return results;
    }
    
}