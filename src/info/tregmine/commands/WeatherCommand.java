package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class WeatherCommand extends AbstractCommand
{
    public WeatherCommand(Tregmine tregmine)
    {
        super(tregmine, "weather");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isDonator()) {
            return true;
        }
        if (args.length != 1) {
            player.resetPlayerWeather();
            return true;
        }

        WeatherType type = WeatherType.valueOf(args[0]);
        player.setPlayerWeather(type);

        return true;
    }
}
