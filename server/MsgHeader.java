package NetworkProgramming.server;

/**
 * message header
 * 
 * @author imrui
 *
 */
public interface MsgHeader {
	// error header
	public static final byte FAIL = 0;
	// login msg header
	public static final byte LOGINHEADER = 1;
	// register msg header
	public static final byte REGISTERHEADER = 2;
	// confirmation header
	public static final byte SUCCESS = 3;
	// update friend list header
	public static final byte FRIENDLIST = 4;
	// new message header
	public static final byte NEWMSG = 5;
	// new image header
	public static final byte NEWIMG = 6;
	

}
