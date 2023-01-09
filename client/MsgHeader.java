package NetworkProgramming.client;

/**
 * message header
 * 
 * @author imrui
 *
 */
public interface MsgHeader {
	// login msg header
	public static final byte LOGINHEADER = 1;
	// register msg header
	public static final byte REGISTERHEADER = 2;
	// confirmation header
	public static final byte SUCCESS = 3;
	// error header
	public static final byte FAIL = 0;

}
