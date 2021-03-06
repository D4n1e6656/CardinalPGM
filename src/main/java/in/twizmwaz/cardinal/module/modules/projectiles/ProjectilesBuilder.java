package in.twizmwaz.cardinal.module.modules.projectiles;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class ProjectilesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        EntityType projectile = EntityType.ARROW;
        double velocityMod = 1.0;
        List<PotionEffect> potionEffects = new ArrayList<>();
        ModuleCollection results = new ModuleCollection();
        for (Element projectiles : match.getDocument().getRootElement().getChildren("modifybowprojectile")) {
            try {
                projectile = EntityType.valueOf(StringUtils.technicalName(projectiles.getChild("projectile").getText()));
            } catch (NullPointerException ex) {

            }
            try {
                velocityMod = Double.parseDouble(projectiles.getChild("velocityMod").getText());
            } catch (NullPointerException ex) {

            }
        }
        results.add(new Projectiles(projectile, velocityMod, potionEffects));
        return results;
    }

}
