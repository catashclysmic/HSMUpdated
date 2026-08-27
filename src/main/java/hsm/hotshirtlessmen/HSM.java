package hsm.hotshirtlessmen;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class HSM implements ModInitializer {
    public static final String MOD_ID = "hotshirtlessmen";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("HSM injected!");
    }

    public static boolean compareComponents(DataComponentMap a, DataComponentMap b) {
        byte flags = 0;
        for (TypedDataComponent<?> x : a)
            for (TypedDataComponent<?> y : b) {
                if (x.type() == y.type() && !Objects.equals(x.value().toString(), y.value().toString())) {
                    String type = x.type().toString();
                    if (type.equals("minecraft:damage") || type.equals("minecraft:lore"))
                        flags |= 2;
                    else if (type.equals("minecraft:custom_data"))
                        flags |= (byte) (compareUUIDs(x.value().toString(), y.value().toString()) ? 4 : 0);
                    else flags |= 1;
                }
            }
        return (flags & 6) != 0 && (flags & 1) == 0;
    }

    public static boolean compareUUIDs(String a, String b) {
        try { return getUUID(a).equals(getUUID(b)); }
        catch (Exception e) { LOGGER.error(String.valueOf(e)); return false; }
    }

    private static String getUUID(String str) {
        if (!str.contains("uuid:")) return str;
        int start = str.indexOf('"', str.indexOf("uuid:")) + 1;
        int end = str.indexOf('"', start);
        return end - start == 36 ? str.substring(start, end) : str;
    }
}