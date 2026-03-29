// A company is developing a Smart Home Control App that allows users to control various smart devices using voice commands.
// The system already supports devices that follow the company's standard interface:
// javapublic interface SmartDevice {
//     void turnOn();
//     void turnOff();
// }
// Currently supported devices include Smart Light, Smart Fan, and Smart AC. All of them correctly implement SmartDevice. 
// The company now wants to integrate third-party smart devices from other manufacturers. However, these devices do not follow the SmartDevice interface.
// 1. OldSmartBulb
// javapublic class OldSmartBulb {
//     public void powerOn() { }
//     public void powerOff() { }
// }
// 2. LegacyHeater
// javapublic class LegacyHeater {
//     public void startHeating() { }
//     public void stopHeating() { }
// }
// However, these devices need to be integrated without modifying the existing interface. 
// Also, third-party device classes cannot be changed, and the app should continue to work using the existing methods. 
// Also, the system should allow adding more third-party devices easily in the future. Your task is to design a solution 
// that allows all third-party devices to be controlled using the SmartDevice interface without modifying their original implementations.

// ─────────────────────────────────────────────────
// Target Interface — already exists, cannot change
// ─────────────────────────────────────────────────
interface SmartDevice {
    void turnOn();
    void turnOff();
}

// ─────────────────────────────────────────────────
// Existing devices — already work fine
// ─────────────────────────────────────────────────
class SmartLight implements SmartDevice {
    @Override
    public void turnOn()  { System.out.println("[SmartLight] Light turned ON."); }
    @Override
    public void turnOff() { System.out.println("[SmartLight] Light turned OFF."); }
}

class SmartFan implements SmartDevice {
    @Override
    public void turnOn()  { System.out.println("[SmartFan] Fan turned ON."); }
    @Override
    public void turnOff() { System.out.println("[SmartFan] Fan turned OFF."); }
}

class SmartAC implements SmartDevice {
    @Override
    public void turnOn()  { System.out.println("[SmartAC] AC turned ON."); }
    @Override
    public void turnOff() { System.out.println("[SmartAC] AC turned OFF."); }
}

// ─────────────────────────────────────────────────
// Adaptees — third-party, CANNOT be modified
// ─────────────────────────────────────────────────
class OldSmartBulb {
    public void powerOn()  { System.out.println("[OldSmartBulb] Bulb powered ON."); }
    public void powerOff() { System.out.println("[OldSmartBulb] Bulb powered OFF."); }
}

class LegacyHeater {
    public void startHeating() { System.out.println("[LegacyHeater] Heater started."); }
    public void stopHeating()  { System.out.println("[LegacyHeater] Heater stopped."); }
}

// ─────────────────────────────────────────────────
// Adapter 1 — wraps OldSmartBulb → SmartDevice
// ─────────────────────────────────────────────────
class OldSmartBulbAdapter implements SmartDevice {
    private OldSmartBulb bulb;   // ← holds the incompatible object

    public OldSmartBulbAdapter(OldSmartBulb bulb) {
        this.bulb = bulb;
    }

    @Override
    public void turnOn()  { bulb.powerOn(); }   // translate call
    @Override
    public void turnOff() { bulb.powerOff(); }  // translate call
}

// ─────────────────────────────────────────────────
// Adapter 2 — wraps LegacyHeater → SmartDevice
// ─────────────────────────────────────────────────
class LegacyHeaterAdapter implements SmartDevice {
    private LegacyHeater heater;

    public LegacyHeaterAdapter(LegacyHeater heater) {
        this.heater = heater;
    }

    @Override
    public void turnOn()  { heater.startHeating(); }  // translate call
    @Override
    public void turnOff() { heater.stopHeating(); }   // translate call
}

// ─────────────────────────────────────────────────
// Main — voice command controller
// ─────────────────────────────────────────────────
public class Online22B2 {

    // Simulated voice command — works on ANY SmartDevice
    static void voiceCommand(SmartDevice device, String command) {
        System.out.print("Voice Command [" + command + "] → ");
        if (command.equalsIgnoreCase("on")) {
            device.turnOn();
        } else {
            device.turnOff();
        }
    }

    public static void main(String[] args) {

        System.out.println("======= Smart Home Control App =======\n");

        // Native devices — work directly
        SmartDevice light = new SmartLight();
        SmartDevice fan   = new SmartFan();
        SmartDevice ac    = new SmartAC();

        // Third-party devices — wrapped in Adapters
        SmartDevice oldBulb = new OldSmartBulbAdapter(new OldSmartBulb());
        SmartDevice heater  = new LegacyHeaterAdapter(new LegacyHeater());

        // All devices controlled UNIFORMLY via SmartDevice interface
        SmartDevice[] allDevices = { light, fan, ac, oldBulb, heater };

        System.out.println("--- Turning everything ON ---");
        for (SmartDevice d : allDevices) {
            voiceCommand(d, "on");
        }

        System.out.println("\n--- Turning everything OFF ---");
        for (SmartDevice d : allDevices) {
            voiceCommand(d, "off");
        }
    }
}
