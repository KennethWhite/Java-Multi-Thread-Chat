package commandP;
/**
 * Interface used as a template for commands: perform, getName, help.
 * perform: perform the operation the command is designed to do.
 * getName: used to get the name of the command.
 * help: used to describe what the command does.
 *
 * Created by Daric on 11/6/2016.
 */
public interface Icommands {
    public String perform();

    public String getName();

    public String help();
}
