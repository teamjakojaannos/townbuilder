package jakojaannos.townbuilder.block;

import lombok.Getter;
import lombok.var;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: move this to some other package
public class StructureBlueprint {

    public static void main(String[] args) {
        System.out.println("Testing StructureBlueprint class\n");


        // creating blueprint
        System.out.println("Creating blueprint...");
        StructureBlueprint bp = createTestBp();
        System.out.println("Blueprint created!\n");
        System.out.printf("Blueprint size: %dx%dx%d\n",
                          bp.getWidth(), bp.getHeight(), bp.getLength());


        // testing getBlockAt(...)
        int x = 4, y = 1, z = 4;
        String block = bp.getBlockAt(x, y, z).orElse("???");
        System.out.printf("Block at (%d,%d,%d): %s\n\n", x, y, z, block);


        // testing getBlocksInCube(...)
        int x1 = 0, x2 = 3,
                y1 = 0, y2 = 2,
                z1 = 0, z2 = 1;
        System.out.printf("Getting a cube from (%d,%d,%d) to (%d,%d,%d)\n",
                          x1, y1, z1, x2, y2, z2);
        String[][][] cube = bp.getBlocksInCube(x1, y1, z1, x2, y2, z2);

        System.out.println("Printing the cube:");
        System.out.println(printLayers(cube));
        System.out.println("---");

        System.out.println("Printing the layers side by side:");
        System.out.println(printLayersSideBySide(cube));
        System.out.println("---");

        System.out.println("Creating another blueprint...");
        StructureBlueprint bp2 = createTestBp2();
        System.out.println("Blueprint created!");

        System.out.println("Printing cube #2:");
        String[][][] cube2 = bp2.getBlocks();
        System.out.println(printLayersSideBySide(cube2));
        System.out.println("---");


        System.out.println("\nDone");
    }

    //public static final StructureBlueprint TEST_BP;

    private static StructureBlueprint createTestBp() {
        String[][] lrs = new String[][]{
                new String[]{
                        "      ",
                        "      ",
                        "  S   ",
                        "      ",
                        "      ",
                },
                new String[]{
                        "      ",
                        "      ",
                        "  SS  ",
                        "      ",
                        "      ",
                },
                new String[]{
                        "      ",
                        " SSSS ",
                        " SSSS ",
                        " SSSS ",
                        "      ",
                },
                new String[]{
                        "SSSSSS",
                        "SSSSSS",
                        "SSSSSS",
                        "SSSSSS",
                        "SSSSSS",
                },
                new String[]{
                        "PPPPPP",
                        "P    P",
                        "P    G",
                        "P    G",
                        "PPPP P",
                },
                new String[]{
                        "PPPPPP",
                        "P    P",
                        "P    P",
                        "P    P",
                        "PPPP P",
                },
                new String[]{
                        "CCCCCC",
                        "CCCCCC",
                        "CCCCCC",
                        "CCCCCC",
                        "CCCCCC"
                }
        };
        StructureBlueprint bp = new StructureBlueprint(lrs);


        bp.setKey('C', "Cobblestone");
        bp.setKey('P', "Planks");
        bp.setKey('G', "Glass");
        bp.setKey('S', "Stone");

        /*bp.setKey('C', Blocks.COBBLESTONE.getDefaultState());
        bp.setKey('P', Blocks.OAK_PLANKS.getDefaultState());
        bp.setKey('G', Blocks.GLASS.getDefaultState());
        bp.setKey('S', Blocks.STONE.getDefaultState());*/

        return bp;
    }

    private static StructureBlueprint createTestBp2() {
        String[][] lrs = new String[][]{
                new String[]{
                        "      SSS      ",
                        "      SSS      ",
                        "      SSS      ",
                },
                new String[]{
                        "CCCPPPCCCPPPCCC",
                        "CCCPPPCCCPPPCCC",
                        "CCCPPPCCCPPPCCC",
                }
        };
        StructureBlueprint bp = new StructureBlueprint(lrs);


        bp.setKey('C', "Cobblestone");
        bp.setKey('P', "Planks");
        bp.setKey('G', "Glass");
        bp.setKey('S', "Stone");

        return bp;
    }


    private String[][] layers;
    //private Map<Character, BlockState> keys;
    private Map<Character, String> keys;

    @Getter
    private final int width, height, length;


    public StructureBlueprint(String[][] layers) {
        // check that all dimensions are same (that is, all arrays are same length, string same size, etc)
        this.layers = layers;
        this.keys = new HashMap<>();

        //setKey(' ', Blocks.AIR.getDefaultState());
        setKey(' ', "Air");


        // layers explained
        // String[][] -> Cube
        // String[]   -> Plane
        // String     -> Row of blocks
        // Char       -> Block


        // height = number of layers
        this.height = layers.length;
        if (height == 0) {
            // throw some kind of error here!
        }

        // length = number of rows in a plane
        this.length = layers[0].length;
        if (length == 0) {
            // throw error
        }

        // width = number of blocks in a row
        this.width = layers[0][0].length();
        if (width == 0) {
            // throw error
        }


        // make sure all planes have same amount of rows, and that all rows have same amount of blocks
        for (var plane : layers) {
            if (plane.length != length) {
                // ERROR: deviant amount of rows in plane!
            }

            for (var row : plane) {
                if (row.length() != width) {
                    // ERROR: deviant amount of blocks in row!
                }
            }
        }


    }


    // this method won't do any bound checking, do that yourself!
    private char getCharAt(int x, int y, int z) {
        // if you want to change coordinate system of this class, do stuff here

        // CURRENT COORDINATE SYSTEM: (0,0,0) is bottom layer, left backside corner (left-top when viewing from above)
        return layers[height - y - 1][z].charAt(x);
    }


    public void setKey(char key, String state) {
        keys.put(key, state);
    }


    public Optional<String> getBlockAt(int x, int y, int z) {
        // bound-check
        if (x < 0 || y < 0 || z < 0) return Optional.empty();
        if (x >= width || y >= height || z >= length) return Optional.empty();

        char c = getCharAt(x, y, z);
        // could also return AIR blockstate
        return Optional.ofNullable(keys.get(c));
    }


    public String[][][] getBlocksInCube(int x1, int y1, int z1, int x2, int y2, int z2) {
        if (Math.min(x1, x2) < 0 || Math.min(y1, y2) < 0 || Math.min(z1, z2) < 0 || //
                Math.max(x1, x2) > width || Math.max(y1, y2) > height || Math.max(z1, z2) > length) {
            // one of the arguments is less than 0 or out of bounds, throw an error?

            return new String[0][0][0];
        }


        // width, height and length
        int cw = Math.abs(x1 - x2) + 1,
                ch = Math.abs(y1 - y2) + 1,
                cl = Math.abs(z1 - z2) + 1;


        String[][][] result = new String[ch][cl][cw];


        for (int xx = 0; xx < cw; xx++) {
            int xc = Math.min(x1, x2) + xx;

            for (int yy = 0; yy < ch; yy++) {
                int yc = Math.min(y1, y2) + yy;

                for (int zz = 0; zz < cl; zz++) {
                    int zc = Math.min(z1, z2) + zz;

                    char c = getCharAt(xc, yc, zc);
                    // if key was not found, do we throw an error or change the missing block to air?
                    //BlockState bs = keys.getOrDefault(c, Blocks.AIR.getDefaultState());
                    String bs = keys.getOrDefault(c, "Air");

                    result[yy][zz][xx] = bs;
                }
            }
        }

        return result;
    }


    public String[][][] getBlocks() {
        return getBlocksInCube(0, 0, 0, width - 1, height - 1, length - 1);
    }

    // ==================== STUFF FOR TESTING/DEBUGGING =============================

    public static String printLayersSideBySide(String[][][] cube) {
        StringBuilder sb = new StringBuilder();

        int width = cube[0][0].length;
        int maxWidth = Math.max(width, "Layer #0".length());

        for (int yy = 0; yy < cube.length; yy++) {
            sb.append("Layer #").append(yy).append("  ");

            // add extra spaces if needed
            for (int i = 0; i < maxWidth - "Layer #0".length(); i++) {
                sb.append(" ");
            }
        }


        for (int zz = 0; zz < cube[0].length; zz++) {
            sb.append("\n");

            for (int yy = 0; yy < cube.length; yy++) {
                for (int xx = 0; xx < cube[0][0].length; xx++) {
                    sb.append(cube[yy][zz][xx].charAt(0));
                }

                sb.append("  ");

                //add extra spaces if needed
                for (int i = 0; i < maxWidth - width; i++) {
                    sb.append(" ");
                }
            }
        }


        return sb.toString();
    }

    public static String printLayers(String[][][] cube) {
        StringBuilder sb = new StringBuilder();

        for (int yy = 0; yy < cube.length; yy++) {
            sb.append("Layer #").append(yy).append("\n");


            for (int zz = 0; zz < cube[yy].length; zz++) {

                for (int xx = 0; xx < cube[yy][zz].length; xx++) {
                    char ch = cube[yy][zz][xx].charAt(0);
                    sb.append(ch);
                }
                sb.append("\n");

            }
        }

        // remove last line break
        sb.deleteCharAt(sb.length() - 1);


        return sb.toString();
    }


}
