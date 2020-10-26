package ru.icc.td.tabbypdf2.detect.processing.verification;

public class Model {
    public static double[] score(double[] input) {
        double[] var0;
        if ((input[6]) <= (0.75)) {
            if ((input[23]) <= (0.5395201742649078)) {
                var0 = new double[]{1.0, 0.0};
            } else {
                var0 = new double[]{0.8085106382978723, 0.19148936170212766};
            }
        } else {
            if ((input[26]) <= (0.7028985619544983)) {
                if ((input[25]) <= (25.5)) {
                    if ((input[4]) <= (1.5)) {
                        var0 = new double[]{0.8941176470588236, 0.10588235294117648};
                    } else {
                        var0 = new double[]{1.0, 0.0};
                    }
                } else {
                    var0 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            } else {
                if ((input[18]) <= (27.0)) {
                    var0 = new double[]{0.7378640776699029, 0.2621359223300971};
                } else {
                    if ((input[24]) <= (0.07970617339015007)) {
                        var0 = new double[]{0.5846153846153846, 0.4153846153846154};
                    } else {
                        if ((input[14]) <= (251.9317398071289)) {
                            var0 = new double[]{0.0, 1.0};
                        } else {
                            var0 = new double[]{0.0, 1.0};
                        }
                    }
                }
            }
        }
        double[] var1;
        if ((input[5]) <= (0.5)) {
            if ((input[24]) <= (0.4167269468307495)) {
                if ((input[0]) <= (26.0)) {
                    var1 = new double[]{0.9440993788819876, 0.055900621118012424};
                } else {
                    if ((input[23]) <= (0.6360114216804504)) {
                        var1 = new double[]{0.0, 1.0};
                    } else {
                        var1 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                if ((input[21]) <= (0.5)) {
                    var1 = new double[]{1.0, 0.0};
                } else {
                    var1 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        } else {
            if ((input[20]) <= (6.5)) {
                if ((input[12]) <= (59.242780685424805)) {
                    var1 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    if ((input[9]) <= (522.4771423339844)) {
                        if ((input[4]) <= (1.5)) {
                            var1 = new double[]{0.0, 1.0};
                        } else {
                            var1 = new double[]{0.0, 1.0};
                        }
                    } else {
                        if ((input[23]) <= (0.7102137804031372)) {
                            if ((input[14]) <= (288.8992004394531)) {
                                var1 = new double[]{0.0, 1.0};
                            } else {
                                var1 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var1 = new double[]{1.0, 0.0};
                        }
                    }
                }
            } else {
                var1 = new double[]{0.8941176470588236, 0.10588235294117648};
            }
        }
        double[] var2;
        if ((input[5]) <= (0.5)) {
            if ((input[13]) <= (384.2357482910156)) {
                if ((input[9]) <= (451.1437225341797)) {
                    var2 = new double[]{1.0, 0.0};
                } else {
                    if ((input[19]) <= (1.632867157459259)) {
                        var2 = new double[]{1.0, 0.0};
                    } else {
                        if ((input[20]) <= (5.5)) {
                            var2 = new double[]{0.0, 1.0};
                        } else {
                            var2 = new double[]{1.0, 0.0};
                        }
                    }
                }
            } else {
                if ((input[10]) <= (116.7230339050293)) {
                    if ((input[12]) <= (90.2875747680664)) {
                        var2 = new double[]{0.0, 1.0};
                    } else {
                        var2 = new double[]{0.0, 1.0};
                    }
                } else {
                    var2 = new double[]{0.8636363636363636, 0.13636363636363638};
                }
            }
        } else {
            if ((input[26]) <= (0.49230770766735077)) {
                var2 = new double[]{0.8941176470588236, 0.10588235294117648};
            } else {
                if ((input[21]) <= (0.5)) {
                    var2 = new double[]{0.6785714285714286, 0.32142857142857145};
                } else {
                    if ((input[11]) <= (282.7858428955078)) {
                        if ((input[24]) <= (0.32544445991516113)) {
                            var2 = new double[]{0.0, 1.0};
                        } else {
                            var2 = new double[]{0.8085106382978723, 0.19148936170212766};
                        }
                    } else {
                        if ((input[14]) <= (260.77271270751953)) {
                            var2 = new double[]{0.0, 1.0};
                        } else {
                            var2 = new double[]{0.0, 1.0};
                        }
                    }
                }
            }
        }
        double[] var3;
        if ((input[23]) <= (0.7429177165031433)) {
            if ((input[17]) <= (6.159138202667236)) {
                var3 = new double[]{1.0, 0.0};
            } else {
                if ((input[1]) <= (1.5)) {
                    var3 = new double[]{1.0, 0.0};
                } else {
                    if ((input[13]) <= (142.45621490478516)) {
                        var3 = new double[]{0.0, 1.0};
                    } else {
                        var3 = new double[]{0.0, 1.0};
                    }
                }
            }
        } else {
            if ((input[18]) <= (107.0)) {
                if ((input[13]) <= (240.7031021118164)) {
                    var3 = new double[]{0.8941176470588236, 0.10588235294117648};
                } else {
                    var3 = new double[]{1.0, 0.0};
                }
            } else {
                var3 = new double[]{0.8085106382978723, 0.19148936170212766};
            }
        }
        double[] var4;
        if ((input[3]) <= (0.8801843225955963)) {
            if ((input[13]) <= (422.54966735839844)) {
                var4 = new double[]{1.0, 0.0};
            } else {
                var4 = new double[]{0.8085106382978723, 0.19148936170212766};
            }
        } else {
            if ((input[26]) <= (0.7028985619544983)) {
                if ((input[16]) <= (365.47279357910156)) {
                    var4 = new double[]{0.9440993788819876, 0.055900621118012424};
                } else {
                    var4 = new double[]{0.7378640776699029, 0.2621359223300971};
                }
            } else {
                if ((input[18]) <= (27.0)) {
                    var4 = new double[]{0.7378640776699029, 0.2621359223300971};
                } else {
                    if ((input[23]) <= (0.9414108395576477)) {
                        if ((input[25]) <= (19.0)) {
                            var4 = new double[]{0.0, 1.0};
                        } else {
                            var4 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var4 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            }
        }
        double[] var5;
        if ((input[18]) <= (21.0)) {
            if ((input[23]) <= (0.7315178513526917)) {
                if ((input[2]) <= (4.0)) {
                    var5 = new double[]{1.0, 0.0};
                } else {
                    var5 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            } else {
                var5 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[25]) <= (52.5)) {
                if ((input[11]) <= (276.3533630371094)) {
                    var5 = new double[]{0.0, 1.0};
                } else {
                    if ((input[5]) <= (0.5)) {
                        if ((input[24]) <= (0.24310444295406342)) {
                            var5 = new double[]{0.8941176470588236, 0.10588235294117648};
                        } else {
                            var5 = new double[]{0.5846153846153846, 0.4153846153846154};
                        }
                    } else {
                        if ((input[12]) <= (60.52959632873535)) {
                            var5 = new double[]{0.8085106382978723, 0.19148936170212766};
                        } else {
                            var5 = new double[]{0.296875, 0.7031249999999999};
                        }
                    }
                }
            } else {
                if ((input[23]) <= (0.20160150527954102)) {
                    var5 = new double[]{0.0, 1.0};
                } else {
                    var5 = new double[]{0.0, 1.0};
                }
            }
        }
        double[] var6;
        if ((input[24]) <= (0.44032448530197144)) {
            if ((input[26]) <= (0.550000011920929)) {
                if ((input[21]) <= (0.5)) {
                    if ((input[10]) <= (145.64891815185547)) {
                        var6 = new double[]{0.8085106382978723, 0.19148936170212766};
                    } else {
                        var6 = new double[]{1.0, 0.0};
                    }
                } else {
                    var6 = new double[]{0.8941176470588236, 0.10588235294117648};
                }
            } else {
                if ((input[4]) <= (9.0)) {
                    if ((input[21]) <= (0.5)) {
                        var6 = new double[]{0.5846153846153846, 0.4153846153846154};
                    } else {
                        if ((input[16]) <= (173.80287170410156)) {
                            var6 = new double[]{0.0, 1.0};
                        } else {
                            var6 = new double[]{0.0, 1.0};
                        }
                    }
                } else {
                    var6 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        } else {
            if ((input[2]) <= (22.5)) {
                var6 = new double[]{1.0, 0.0};
            } else {
                var6 = new double[]{0.8941176470588236, 0.10588235294117648};
            }
        }
        double[] var7;
        if ((input[17]) <= (61.47026062011719)) {
            if ((input[21]) <= (0.5)) {
                if ((input[4]) <= (0.5)) {
                    var7 = new double[]{1.0, 0.0};
                } else {
                    var7 = new double[]{0.4578313253012048, 0.5421686746987951};
                }
            } else {
                if ((input[9]) <= (241.53763580322266)) {
                    var7 = new double[]{0.0, 1.0};
                } else {
                    var7 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[19]) <= (1.632867157459259)) {
                var7 = new double[]{1.0, 0.0};
            } else {
                if ((input[26]) <= (0.9582608640193939)) {
                    if ((input[8]) <= (362.7459259033203)) {
                        if ((input[7]) <= (1.6406959295272827)) {
                            if ((input[24]) <= (0.3475145548582077)) {
                                var7 = new double[]{0.0, 1.0};
                            } else {
                                var7 = new double[]{0.926829268292683, 0.07317073170731707};
                            }
                        } else {
                            var7 = new double[]{1.0, 0.0};
                        }
                    } else {
                        var7 = new double[]{0.5846153846153846, 0.4153846153846154};
                    }
                } else {
                    if ((input[15]) <= (113.85824584960938)) {
                        var7 = new double[]{0.0, 1.0};
                    } else {
                        var7 = new double[]{0.0, 1.0};
                    }
                }
            }
        }
        double[] var8;
        if ((input[6]) <= (0.75)) {
            if ((input[25]) <= (35.0)) {
                var8 = new double[]{1.0, 0.0};
            } else {
                var8 = new double[]{0.0, 1.0};
            }
        } else {
            if ((input[21]) <= (0.5)) {
                if ((input[7]) <= (0.0833333358168602)) {
                    var8 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    var8 = new double[]{0.9440993788819876, 0.055900621118012424};
                }
            } else {
                if ((input[5]) <= (0.5)) {
                    if ((input[10]) <= (49.49709510803223)) {
                        var8 = new double[]{0.8085106382978723, 0.19148936170212766};
                    } else {
                        if ((input[23]) <= (0.9142287075519562)) {
                            var8 = new double[]{0.0, 1.0};
                        } else {
                            var8 = new double[]{1.0, 0.0};
                        }
                    }
                } else {
                    if ((input[23]) <= (0.7593585550785065)) {
                        if ((input[23]) <= (0.20160150527954102)) {
                            var8 = new double[]{0.0, 1.0};
                        } else {
                            var8 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var8 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            }
        }
        double[] var9;
        if ((input[22]) <= (1.25)) {
            if ((input[9]) <= (487.0993194580078)) {
                if ((input[25]) <= (16.5)) {
                    var9 = new double[]{0.9547738693467337, 0.04522613065326633};
                } else {
                    var9 = new double[]{0.0, 1.0};
                }
            } else {
                var9 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[2]) <= (13.5)) {
                var9 = new double[]{0.8636363636363636, 0.13636363636363638};
            } else {
                if ((input[18]) <= (111.0)) {
                    if ((input[7]) <= (0.7195662260055542)) {
                        if ((input[5]) <= (0.5)) {
                            var9 = new double[]{0.0, 1.0};
                        } else {
                            if ((input[25]) <= (25.5)) {
                                var9 = new double[]{0.319327731092437, 0.6806722689075629};
                            } else {
                                var9 = new double[]{0.0, 1.0};
                            }
                        }
                    } else {
                        if ((input[3]) <= (2.596874952316284)) {
                            var9 = new double[]{0.5135135135135135, 0.48648648648648646};
                        } else {
                            var9 = new double[]{1.0, 0.0};
                        }
                    }
                } else {
                    if ((input[26]) <= (0.7981366515159607)) {
                        var9 = new double[]{0.0, 1.0};
                    } else {
                        var9 = new double[]{0.0, 1.0};
                    }
                }
            }
        }
        double[] var10;
        if ((input[25]) <= (28.5)) {
            if ((input[21]) <= (0.5)) {
                if ((input[20]) <= (1.5)) {
                    var10 = new double[]{1.0, 0.0};
                } else {
                    if ((input[7]) <= (0.5307843685150146)) {
                        if ((input[7]) <= (0.0833333358168602)) {
                            var10 = new double[]{0.8636363636363636, 0.13636363636363638};
                        } else {
                            var10 = new double[]{1.0, 0.0};
                        }
                    } else {
                        var10 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                if ((input[20]) <= (3.5)) {
                    if ((input[8]) <= (342.9809112548828)) {
                        var10 = new double[]{0.0, 1.0};
                    } else {
                        var10 = new double[]{0.41304347826086957, 0.5869565217391304};
                    }
                } else {
                    var10 = new double[]{0.926829268292683, 0.07317073170731707};
                }
            }
        } else {
            if ((input[12]) <= (190.89041137695312)) {
                if ((input[5]) <= (0.5)) {
                    var10 = new double[]{0.0, 1.0};
                } else {
                    if ((input[24]) <= (0.10767295211553574)) {
                        if ((input[15]) <= (322.43907165527344)) {
                            var10 = new double[]{0.0, 1.0};
                        } else {
                            var10 = new double[]{0.5846153846153846, 0.4153846153846154};
                        }
                    } else {
                        var10 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                var10 = new double[]{0.8085106382978723, 0.19148936170212766};
            }
        }
        double[] var11;
        if ((input[17]) <= (61.47026062011719)) {
            if ((input[21]) <= (0.5)) {
                if ((input[1]) <= (5.0)) {
                    var11 = new double[]{0.9440993788819876, 0.055900621118012424};
                } else {
                    var11 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[18]) <= (25.0)) {
                    var11 = new double[]{0.0, 1.0};
                } else {
                    var11 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[6]) <= (1.5)) {
                if ((input[13]) <= (419.3718719482422)) {
                    if ((input[21]) <= (0.5)) {
                        var11 = new double[]{1.0, 0.0};
                    } else {
                        if ((input[0]) <= (29.0)) {
                            var11 = new double[]{1.0, 0.0};
                        } else {
                            var11 = new double[]{0.0, 1.0};
                        }
                    }
                } else {
                    if ((input[12]) <= (80.4349479675293)) {
                        var11 = new double[]{0.5846153846153846, 0.4153846153846154};
                    } else {
                        var11 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                var11 = new double[]{1.0, 0.0};
            }
        }
        double[] var12;
        if ((input[19]) <= (1.1200000047683716)) {
            var12 = new double[]{1.0, 0.0};
        } else {
            if ((input[7]) <= (1.5859842896461487)) {
                if ((input[3]) <= (0.4563375189900398)) {
                    var12 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    if ((input[20]) <= (3.5)) {
                        if ((input[20]) <= (2.5)) {
                            var12 = new double[]{0.0, 1.0};
                        } else {
                            var12 = new double[]{0.0, 1.0};
                        }
                    } else {
                        if ((input[12]) <= (110.1159439086914)) {
                            var12 = new double[]{1.0, 0.0};
                        } else {
                            if ((input[9]) <= (505.64247131347656)) {
                                var12 = new double[]{0.0, 1.0};
                            } else {
                                var12 = new double[]{0.5135135135135135, 0.48648648648648646};
                            }
                        }
                    }
                }
            } else {
                var12 = new double[]{1.0, 0.0};
            }
        }
        double[] var13;
        if ((input[26]) <= (0.7028985619544983)) {
            if ((input[11]) <= (395.5934143066406)) {
                if ((input[6]) <= (1.5)) {
                    if ((input[17]) <= (60.41570854187012)) {
                        var13 = new double[]{0.8941176470588236, 0.10588235294117648};
                    } else {
                        var13 = new double[]{1.0, 0.0};
                    }
                } else {
                    var13 = new double[]{1.0, 0.0};
                }
            } else {
                var13 = new double[]{0.6785714285714286, 0.32142857142857145};
            }
        } else {
            if ((input[5]) <= (0.5)) {
                if ((input[8]) <= (326.4039611816406)) {
                    var13 = new double[]{0.0, 1.0};
                } else {
                    var13 = new double[]{0.7378640776699029, 0.2621359223300971};
                }
            } else {
                if ((input[23]) <= (0.7823767960071564)) {
                    if ((input[15]) <= (94.2455825805664)) {
                        var13 = new double[]{0.0, 1.0};
                    } else {
                        var13 = new double[]{0.0, 1.0};
                    }
                } else {
                    var13 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        }
        double[] var14;
        if ((input[15]) <= (104.69190216064453)) {
            if ((input[22]) <= (1.0)) {
                var14 = new double[]{1.0, 0.0};
            } else {
                if ((input[9]) <= (463.2272186279297)) {
                    var14 = new double[]{0.0, 1.0};
                } else {
                    var14 = new double[]{0.8941176470588236, 0.10588235294117648};
                }
            }
        } else {
            if ((input[11]) <= (320.0438995361328)) {
                if ((input[2]) <= (16.0)) {
                    if ((input[5]) <= (0.5)) {
                        var14 = new double[]{1.0, 0.0};
                    } else {
                        var14 = new double[]{0.8941176470588236, 0.10588235294117648};
                    }
                } else {
                    if ((input[26]) <= (0.7424242496490479)) {
                        var14 = new double[]{0.8941176470588236, 0.10588235294117648};
                    } else {
                        var14 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                if ((input[0]) <= (23.5)) {
                    if ((input[9]) <= (494.3846130371094)) {
                        var14 = new double[]{0.0, 1.0};
                    } else {
                        if ((input[12]) <= (71.17329788208008)) {
                            var14 = new double[]{0.0, 1.0};
                        } else {
                            var14 = new double[]{0.8636363636363636, 0.13636363636363638};
                        }
                    }
                } else {
                    var14 = new double[]{0.0, 1.0};
                }
            }
        }
        double[] var15;
        if ((input[11]) <= (466.12205505371094)) {
            if ((input[18]) <= (27.0)) {
                if ((input[18]) <= (22.0)) {
                    if ((input[20]) <= (1.5)) {
                        var15 = new double[]{1.0, 0.0};
                    } else {
                        if ((input[3]) <= (0.9285714328289032)) {
                            var15 = new double[]{1.0, 0.0};
                        } else {
                            var15 = new double[]{0.926829268292683, 0.07317073170731707};
                        }
                    }
                } else {
                    var15 = new double[]{0.6785714285714286, 0.32142857142857145};
                }
            } else {
                if ((input[13]) <= (374.3860321044922)) {
                    var15 = new double[]{0.42339832869080785, 0.5766016713091922};
                } else {
                    if ((input[0]) <= (48.5)) {
                        if ((input[3]) <= (1.421875)) {
                            var15 = new double[]{0.0, 1.0};
                        } else {
                            var15 = new double[]{0.8085106382978723, 0.19148936170212766};
                        }
                    } else {
                        var15 = new double[]{0.0, 1.0};
                    }
                }
            }
        } else {
            var15 = new double[]{0.0, 1.0};
        }
        double[] var16;
        if ((input[4]) <= (0.5)) {
            var16 = new double[]{1.0, 0.0};
        } else {
            if ((input[6]) <= (1.5)) {
                if ((input[1]) <= (14.5)) {
                    if ((input[14]) <= (624.3181457519531)) {
                        if ((input[20]) <= (3.5)) {
                            if ((input[11]) <= (307.27333068847656)) {
                                var16 = new double[]{0.8085106382978723, 0.19148936170212766};
                            } else {
                                var16 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var16 = new double[]{1.0, 0.0};
                        }
                    } else {
                        var16 = new double[]{1.0, 0.0};
                    }
                } else {
                    if ((input[14]) <= (533.0682678222656)) {
                        var16 = new double[]{0.0, 1.0};
                    } else {
                        if ((input[16]) <= (398.6566619873047)) {
                            var16 = new double[]{0.8085106382978723, 0.19148936170212766};
                        } else {
                            var16 = new double[]{0.0, 1.0};
                        }
                    }
                }
            } else {
                var16 = new double[]{1.0, 0.0};
            }
        }
        double[] var17;
        if ((input[10]) <= (103.70027542114258)) {
            if ((input[0]) <= (29.0)) {
                if ((input[22]) <= (1.0)) {
                    var17 = new double[]{1.0, 0.0};
                } else {
                    if ((input[3]) <= (1.0138888955116272)) {
                        if ((input[8]) <= (308.27838134765625)) {
                            var17 = new double[]{0.0, 1.0};
                        } else {
                            var17 = new double[]{0.6785714285714286, 0.32142857142857145};
                        }
                    } else {
                        var17 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                if ((input[25]) <= (52.5)) {
                    if ((input[14]) <= (420.8082275390625)) {
                        var17 = new double[]{0.5846153846153846, 0.4153846153846154};
                    } else {
                        if ((input[17]) <= (30.157913208007812)) {
                            var17 = new double[]{0.0, 1.0};
                        } else {
                            var17 = new double[]{0.0, 1.0};
                        }
                    }
                } else {
                    var17 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[18]) <= (27.0)) {
                if ((input[13]) <= (465.6352081298828)) {
                    if ((input[15]) <= (247.00951385498047)) {
                        var17 = new double[]{1.0, 0.0};
                    } else {
                        var17 = new double[]{0.926829268292683, 0.07317073170731707};
                    }
                } else {
                    var17 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            } else {
                if ((input[5]) <= (0.5)) {
                    var17 = new double[]{0.5135135135135135, 0.48648648648648646};
                } else {
                    if ((input[8]) <= (319.63624572753906)) {
                        var17 = new double[]{0.5135135135135135, 0.48648648648648646};
                    } else {
                        var17 = new double[]{0.0, 1.0};
                    }
                }
            }
        }
        double[] var18;
        if ((input[23]) <= (0.7429177165031433)) {
            if ((input[20]) <= (1.5)) {
                var18 = new double[]{1.0, 0.0};
            } else {
                if ((input[3]) <= (0.4563375189900398)) {
                    var18 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    if ((input[14]) <= (206.1433563232422)) {
                        var18 = new double[]{0.0, 1.0};
                    } else {
                        var18 = new double[]{0.0, 1.0};
                    }
                }
            }
        } else {
            if ((input[11]) <= (320.0438995361328)) {
                var18 = new double[]{1.0, 0.0};
            } else {
                if ((input[22]) <= (1.5)) {
                    var18 = new double[]{1.0, 0.0};
                } else {
                    var18 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        }
        double[] var19;
        if ((input[13]) <= (374.3860321044922)) {
            if ((input[15]) <= (86.54496002197266)) {
                var19 = new double[]{0.9440993788819876, 0.055900621118012424};
            } else {
                if ((input[16]) <= (338.24635314941406)) {
                    if ((input[16]) <= (289.5593719482422)) {
                        if ((input[14]) <= (431.2615051269531)) {
                            if ((input[19]) <= (0.8095238357782364)) {
                                var19 = new double[]{1.0, 0.0};
                            } else {
                                var19 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var19 = new double[]{1.0, 0.0};
                        }
                    } else {
                        if ((input[23]) <= (0.2855484336614609)) {
                            var19 = new double[]{0.8085106382978723, 0.19148936170212766};
                        } else {
                            var19 = new double[]{0.0, 1.0};
                        }
                    }
                } else {
                    if ((input[0]) <= (29.0)) {
                        var19 = new double[]{1.0, 0.0};
                    } else {
                        var19 = new double[]{0.0, 1.0};
                    }
                }
            }
        } else {
            if ((input[3]) <= (0.7094431221485138)) {
                var19 = new double[]{0.926829268292683, 0.07317073170731707};
            } else {
                if ((input[16]) <= (521.8912353515625)) {
                    var19 = new double[]{0.0, 1.0};
                } else {
                    if ((input[5]) <= (0.5)) {
                        var19 = new double[]{0.0, 1.0};
                    } else {
                        if ((input[1]) <= (151.0)) {
                            var19 = new double[]{0.0, 1.0};
                        } else {
                            var19 = new double[]{0.8085106382978723, 0.19148936170212766};
                        }
                    }
                }
            }
        }
        double[] var20;
        if ((input[18]) <= (21.0)) {
            var20 = new double[]{0.9806451612903225, 0.01935483870967742};
        } else {
            if ((input[21]) <= (0.5)) {
                var20 = new double[]{0.6785714285714286, 0.32142857142857145};
            } else {
                if ((input[20]) <= (5.0)) {
                    if ((input[7]) <= (0.27475781738758087)) {
                        var20 = new double[]{0.0, 1.0};
                    } else {
                        if ((input[4]) <= (2.5)) {
                            if ((input[13]) <= (334.4714813232422)) {
                                var20 = new double[]{0.0, 1.0};
                            } else {
                                var20 = new double[]{0.6785714285714286, 0.32142857142857145};
                            }
                        } else {
                            if ((input[15]) <= (239.43435668945312)) {
                                var20 = new double[]{0.0, 1.0};
                            } else {
                                var20 = new double[]{0.0, 1.0};
                            }
                        }
                    }
                } else {
                    var20 = new double[]{0.8636363636363636, 0.13636363636363638};
                }
            }
        }
        double[] var21;
        if ((input[4]) <= (0.5)) {
            var21 = new double[]{1.0, 0.0};
        } else {
            if ((input[14]) <= (712.2421569824219)) {
                if ((input[12]) <= (106.8265609741211)) {
                    if ((input[1]) <= (5.5)) {
                        var21 = new double[]{0.926829268292683, 0.07317073170731707};
                    } else {
                        if ((input[8]) <= (322.7865753173828)) {
                            if ((input[11]) <= (237.0435791015625)) {
                                var21 = new double[]{0.0, 1.0};
                            } else {
                                var21 = new double[]{0.8636363636363636, 0.13636363636363638};
                            }
                        } else {
                            if ((input[23]) <= (0.45447273552417755)) {
                                var21 = new double[]{0.0, 1.0};
                            } else {
                                var21 = new double[]{0.0, 1.0};
                            }
                        }
                    }
                } else {
                    if ((input[18]) <= (22.0)) {
                        var21 = new double[]{1.0, 0.0};
                    } else {
                        if ((input[2]) <= (56.0)) {
                            if ((input[14]) <= (393.98797607421875)) {
                                var21 = new double[]{0.0, 1.0};
                            } else {
                                if ((input[15]) <= (240.74078369140625)) {
                                    var21 = new double[]{0.926829268292683, 0.07317073170731707};
                                } else {
                                    var21 = new double[]{0.0, 1.0};
                                }
                            }
                        } else {
                            if ((input[7]) <= (0.02946278266608715)) {
                                var21 = new double[]{0.0, 1.0};
                            } else {
                                var21 = new double[]{0.0, 1.0};
                            }
                        }
                    }
                }
            } else {
                var21 = new double[]{0.0, 1.0};
            }
        }
        double[] var22;
        if ((input[26]) <= (0.7028985619544983)) {
            if ((input[4]) <= (0.5)) {
                var22 = new double[]{1.0, 0.0};
            } else {
                if ((input[20]) <= (3.5)) {
                    if ((input[1]) <= (8.0)) {
                        var22 = new double[]{1.0, 0.0};
                    } else {
                        var22 = new double[]{0.5135135135135135, 0.48648648648648646};
                    }
                } else {
                    var22 = new double[]{1.0, 0.0};
                }
            }
        } else {
            if ((input[18]) <= (27.0)) {
                var22 = new double[]{0.8085106382978724, 0.19148936170212766};
            } else {
                if ((input[7]) <= (1.1870750784873962)) {
                    var22 = new double[]{0.0, 1.0};
                } else {
                    var22 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        }
        double[] var23;
        if ((input[3]) <= (0.8801843225955963)) {
            if ((input[17]) <= (56.8107852935791)) {
                var23 = new double[]{0.8941176470588236, 0.10588235294117648};
            } else {
                var23 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[17]) <= (63.767290115356445)) {
                if ((input[26]) <= (0.375)) {
                    var23 = new double[]{0.0, 1.0};
                } else {
                    var23 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[11]) <= (277.86814880371094)) {
                    var23 = new double[]{0.0, 1.0};
                } else {
                    if ((input[26]) <= (0.9582608640193939)) {
                        if ((input[20]) <= (5.0)) {
                            if ((input[4]) <= (2.5)) {
                                var23 = new double[]{0.7378640776699029, 0.2621359223300971};
                            } else {
                                var23 = new double[]{0.5846153846153846, 0.4153846153846154};
                            }
                        } else {
                            var23 = new double[]{1.0, 0.0};
                        }
                    } else {
                        if ((input[26]) <= (0.9969879388809204)) {
                            var23 = new double[]{0.0, 1.0};
                        } else {
                            if ((input[10]) <= (138.80425262451172)) {
                                var23 = new double[]{0.0, 1.0};
                            } else {
                                var23 = new double[]{0.8085106382978723, 0.19148936170212766};
                            }
                        }
                    }
                }
            }
        }
        double[] var24;
        if ((input[7]) <= (1.5859842896461487)) {
            if ((input[15]) <= (341.2080993652344)) {
                if ((input[19]) <= (1.020000010728836)) {
                    var24 = new double[]{1.0, 0.0};
                } else {
                    if ((input[19]) <= (2.1500000953674316)) {
                        if ((input[1]) <= (4.5)) {
                            var24 = new double[]{0.8085106382978723, 0.19148936170212766};
                        } else {
                            var24 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var24 = new double[]{1.0, 0.0};
                    }
                }
            } else {
                if ((input[21]) <= (0.5)) {
                    var24 = new double[]{0.8941176470588236, 0.10588235294117648};
                } else {
                    var24 = new double[]{0.0, 1.0};
                }
            }
        } else {
            var24 = new double[]{1.0, 0.0};
        }
        double[] var25;
        if ((input[10]) <= (103.70027542114258)) {
            if ((input[5]) <= (0.5)) {
                if ((input[19]) <= (1.6243032217025757)) {
                    var25 = new double[]{0.926829268292683, 0.07317073170731707};
                } else {
                    var25 = new double[]{0.43428571428571433, 0.5657142857142856};
                }
            } else {
                if ((input[20]) <= (9.0)) {
                    var25 = new double[]{0.0, 1.0};
                } else {
                    var25 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        } else {
            if ((input[0]) <= (23.5)) {
                if ((input[1]) <= (5.5)) {
                    var25 = new double[]{1.0, 0.0};
                } else {
                    if ((input[1]) <= (13.0)) {
                        var25 = new double[]{0.0, 1.0};
                    } else {
                        var25 = new double[]{0.8636363636363636, 0.13636363636363638};
                    }
                }
            } else {
                if ((input[7]) <= (1.0671947002410889)) {
                    var25 = new double[]{0.0, 1.0};
                } else {
                    var25 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        }
        double[] var26;
        if ((input[15]) <= (104.69190216064453)) {
            if ((input[19]) <= (1.0874125957489014)) {
                var26 = new double[]{1.0, 0.0};
            } else {
                if ((input[5]) <= (0.5)) {
                    var26 = new double[]{1.0, 0.0};
                } else {
                    var26 = new double[]{0.6785714285714286, 0.32142857142857145};
                }
            }
        } else {
            if ((input[18]) <= (27.0)) {
                if ((input[9]) <= (345.38548278808594)) {
                    var26 = new double[]{1.0, 0.0};
                } else {
                    if ((input[25]) <= (8.5)) {
                        var26 = new double[]{1.0, 0.0};
                    } else {
                        var26 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                if ((input[1]) <= (38.5)) {
                    var26 = new double[]{0.0, 1.0};
                } else {
                    if ((input[4]) <= (5.5)) {
                        if ((input[7]) <= (0.7195662260055542)) {
                            var26 = new double[]{0.0, 1.0};
                        } else {
                            var26 = new double[]{0.5135135135135135, 0.48648648648648646};
                        }
                    } else {
                        var26 = new double[]{0.6785714285714286, 0.32142857142857145};
                    }
                }
            }
        }
        double[] var27;
        if ((input[6]) <= (0.75)) {
            if ((input[2]) <= (20.5)) {
                var27 = new double[]{1.0, 0.0};
            } else {
                var27 = new double[]{0.0, 1.0};
            }
        } else {
            if ((input[2]) <= (13.5)) {
                if ((input[22]) <= (0.5)) {
                    var27 = new double[]{1.0, 0.0};
                } else {
                    if ((input[11]) <= (366.90631103515625)) {
                        var27 = new double[]{0.8636363636363636, 0.13636363636363638};
                    } else {
                        var27 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                if ((input[1]) <= (35.5)) {
                    var27 = new double[]{0.0, 1.0};
                } else {
                    if ((input[18]) <= (111.0)) {
                        if ((input[10]) <= (177.07108306884766)) {
                            if ((input[23]) <= (0.8004679381847382)) {
                                if ((input[17]) <= (35.27894592285156)) {
                                    var27 = new double[]{0.0, 1.0};
                                } else {
                                    var27 = new double[]{0.0, 1.0};
                                }
                            } else {
                                var27 = new double[]{1.0, 0.0};
                            }
                        } else {
                            var27 = new double[]{0.0, 1.0};
                        }
                    } else {
                        if ((input[4]) <= (1.5)) {
                            var27 = new double[]{0.0, 1.0};
                        } else {
                            var27 = new double[]{0.0, 1.0};
                        }
                    }
                }
            }
        }
        double[] var28;
        if ((input[17]) <= (61.47026062011719)) {
            if ((input[21]) <= (0.5)) {
                if ((input[4]) <= (0.5)) {
                    var28 = new double[]{1.0, 0.0};
                } else {
                    var28 = new double[]{0.4578313253012048, 0.5421686746987951};
                }
            } else {
                if ((input[11]) <= (150.21869277954102)) {
                    var28 = new double[]{0.0, 1.0};
                } else {
                    var28 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[7]) <= (1.5859842896461487)) {
                if ((input[11]) <= (277.86814880371094)) {
                    var28 = new double[]{0.0, 1.0};
                } else {
                    if ((input[26]) <= (0.7028985619544983)) {
                        if ((input[18]) <= (70.0)) {
                            var28 = new double[]{1.0, 0.0};
                        } else {
                            var28 = new double[]{0.8085106382978723, 0.19148936170212766};
                        }
                    } else {
                        if ((input[10]) <= (167.86126708984375)) {
                            if ((input[19]) <= (1.7354312539100647)) {
                                var28 = new double[]{0.8085106382978723, 0.19148936170212766};
                            } else {
                                var28 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var28 = new double[]{0.8085106382978723, 0.19148936170212766};
                        }
                    }
                }
            } else {
                var28 = new double[]{1.0, 0.0};
            }
        }
        double[] var29;
        if ((input[3]) <= (0.8801843225955963)) {
            if ((input[24]) <= (0.21391615271568298)) {
                var29 = new double[]{0.6785714285714286, 0.32142857142857145};
            } else {
                var29 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[22]) <= (1.25)) {
                var29 = new double[]{0.8085106382978724, 0.19148936170212766};
            } else {
                if ((input[19]) <= (2.0972962379455566)) {
                    if ((input[19]) <= (1.8047618865966797)) {
                        if ((input[13]) <= (226.46736907958984)) {
                            var29 = new double[]{0.5846153846153846, 0.4153846153846154};
                        } else {
                            if ((input[14]) <= (365.3940734863281)) {
                                var29 = new double[]{0.0, 1.0};
                            } else {
                                var29 = new double[]{0.0, 1.0};
                            }
                        }
                    } else {
                        if ((input[16]) <= (210.36854553222656)) {
                            var29 = new double[]{0.0, 1.0};
                        } else {
                            var29 = new double[]{0.0, 1.0};
                        }
                    }
                } else {
                    var29 = new double[]{0.9547738693467337, 0.04522613065326633};
                }
            }
        }
        double[] var30;
        if ((input[21]) <= (0.5)) {
            if ((input[9]) <= (450.16429138183594)) {
                var30 = new double[]{0.8085106382978724, 0.19148936170212766};
            } else {
                if ((input[25]) <= (21.0)) {
                    var30 = new double[]{1.0, 0.0};
                } else {
                    var30 = new double[]{0.8636363636363636, 0.13636363636363638};
                }
            }
        } else {
            if ((input[17]) <= (78.1162109375)) {
                if ((input[8]) <= (168.31510162353516)) {
                    var30 = new double[]{0.0, 1.0};
                } else {
                    var30 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[17]) <= (84.47154235839844)) {
                    var30 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    if ((input[4]) <= (5.5)) {
                        if ((input[8]) <= (276.26795959472656)) {
                            if ((input[2]) <= (37.0)) {
                                var30 = new double[]{0.8085106382978723, 0.19148936170212766};
                            } else {
                                var30 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var30 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var30 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            }
        }
        double[] var31;
        if ((input[18]) <= (21.0)) {
            if ((input[17]) <= (31.583704948425293)) {
                var31 = new double[]{0.926829268292683, 0.07317073170731707};
            } else {
                var31 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[19]) <= (2.0972962379455566)) {
                if ((input[13]) <= (360.1959991455078)) {
                    if ((input[23]) <= (0.6558404862880707)) {
                        if ((input[4]) <= (1.5)) {
                            var31 = new double[]{0.0, 1.0};
                        } else {
                            var31 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var31 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                } else {
                    if ((input[5]) <= (0.5)) {
                        var31 = new double[]{0.0, 1.0};
                    } else {
                        var31 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                var31 = new double[]{0.9547738693467337, 0.04522613065326633};
            }
        }
        double[] var32;
        if ((input[17]) <= (61.47026062011719)) {
            if ((input[10]) <= (111.21538925170898)) {
                if ((input[23]) <= (0.25854557752609253)) {
                    var32 = new double[]{0.0, 1.0};
                } else {
                    var32 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[24]) <= (0.23154696822166443)) {
                    if ((input[23]) <= (0.45856809616088867)) {
                        var32 = new double[]{0.0, 1.0};
                    } else {
                        var32 = new double[]{0.0, 1.0};
                    }
                } else {
                    if ((input[11]) <= (312.49159240722656)) {
                        var32 = new double[]{1.0, 0.0};
                    } else {
                        var32 = new double[]{0.0, 1.0};
                    }
                }
            }
        } else {
            if ((input[5]) <= (0.5)) {
                if ((input[0]) <= (28.5)) {
                    var32 = new double[]{1.0, 0.0};
                } else {
                    if ((input[1]) <= (135.5)) {
                        if ((input[8]) <= (272.4936828613281)) {
                            var32 = new double[]{0.0, 1.0};
                        } else {
                            var32 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var32 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                var32 = new double[]{0.5467625899280576, 0.4532374100719424};
            }
        }
        double[] var33;
        if ((input[6]) <= (0.75)) {
            if ((input[24]) <= (0.249904066324234)) {
                var33 = new double[]{0.0, 1.0};
            } else {
                var33 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[3]) <= (1.796875)) {
                if ((input[23]) <= (0.7823767960071564)) {
                    if ((input[25]) <= (15.0)) {
                        var33 = new double[]{0.0, 1.0};
                    } else {
                        var33 = new double[]{0.0, 1.0};
                    }
                } else {
                    if ((input[2]) <= (22.5)) {
                        var33 = new double[]{1.0, 0.0};
                    } else {
                        var33 = new double[]{0.8085106382978723, 0.19148936170212766};
                    }
                }
            } else {
                var33 = new double[]{1.0, 0.0};
            }
        }
        double[] var34;
        if ((input[23]) <= (0.7429177165031433)) {
            if ((input[6]) <= (0.5)) {
                if ((input[16]) <= (391.84332275390625)) {
                    var34 = new double[]{1.0, 0.0};
                } else {
                    var34 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[21]) <= (0.5)) {
                    var34 = new double[]{0.0, 1.0};
                } else {
                    var34 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[20]) <= (10.0)) {
                if ((input[9]) <= (475.6132354736328)) {
                    var34 = new double[]{0.926829268292683, 0.07317073170731707};
                } else {
                    var34 = new double[]{1.0, 0.0};
                }
            } else {
                var34 = new double[]{0.8085106382978723, 0.19148936170212766};
            }
        }
        double[] var35;
        if ((input[25]) <= (28.5)) {
            if ((input[19]) <= (1.1315789222717285)) {
                var35 = new double[]{1.0, 0.0};
            } else {
                if ((input[4]) <= (1.5)) {
                    if ((input[9]) <= (482.82989501953125)) {
                        if ((input[25]) <= (17.0)) {
                            var35 = new double[]{0.8085106382978723, 0.19148936170212766};
                        } else {
                            var35 = new double[]{0.0, 1.0};
                        }
                    } else {
                        var35 = new double[]{0.0, 1.0};
                    }
                } else {
                    if ((input[21]) <= (0.5)) {
                        var35 = new double[]{0.8085106382978723, 0.19148936170212766};
                    } else {
                        var35 = new double[]{0.8941176470588236, 0.10588235294117648};
                    }
                }
            }
        } else {
            if ((input[0]) <= (52.5)) {
                if ((input[6]) <= (1.5)) {
                    if ((input[17]) <= (22.17207622528076)) {
                        var35 = new double[]{0.0, 1.0};
                    } else {
                        var35 = new double[]{0.0, 1.0};
                    }
                } else {
                    var35 = new double[]{1.0, 0.0};
                }
            } else {
                if ((input[20]) <= (2.5)) {
                    var35 = new double[]{0.0, 1.0};
                } else {
                    var35 = new double[]{0.0, 1.0};
                }
            }
        }
        double[] var36;
        if ((input[17]) <= (61.47026062011719)) {
            if ((input[3]) <= (0.7094431221485138)) {
                var36 = new double[]{0.9440993788819876, 0.055900621118012424};
            } else {
                if ((input[3]) <= (0.9252971112728119)) {
                    var36 = new double[]{0.0, 1.0};
                } else {
                    var36 = new double[]{0.0, 1.0};
                }
            }
        } else {
            if ((input[12]) <= (158.64185333251953)) {
                if ((input[18]) <= (108.0)) {
                    if ((input[18]) <= (39.0)) {
                        var36 = new double[]{1.0, 0.0};
                    } else {
                        if ((input[19]) <= (2.0786051750183105)) {
                            if ((input[0]) <= (31.0)) {
                                var36 = new double[]{0.0, 1.0};
                            } else {
                                var36 = new double[]{0.0, 1.0};
                            }
                        } else {
                            var36 = new double[]{1.0, 0.0};
                        }
                    }
                } else {
                    if ((input[4]) <= (1.5)) {
                        var36 = new double[]{0.0, 1.0};
                    } else {
                        var36 = new double[]{0.0, 1.0};
                    }
                }
            } else {
                if ((input[23]) <= (0.6389461755752563)) {
                    var36 = new double[]{0.8085106382978723, 0.19148936170212766};
                } else {
                    var36 = new double[]{1.0, 0.0};
                }
            }
        }
        double[] var37;
        if ((input[20]) <= (1.5)) {
            var37 = new double[]{1.0, 0.0};
        } else {
            if ((input[21]) <= (0.5)) {
                if ((input[22]) <= (0.5)) {
                    var37 = new double[]{1.0, 0.0};
                } else {
                    if ((input[2]) <= (15.0)) {
                        var37 = new double[]{0.926829268292683, 0.07317073170731707};
                    } else {
                        var37 = new double[]{0.6785714285714286, 0.32142857142857145};
                    }
                }
            } else {
                if ((input[23]) <= (0.7593585550785065)) {
                    if ((input[12]) <= (61.15865516662598)) {
                        var37 = new double[]{0.0, 1.0};
                    } else {
                        var37 = new double[]{0.0, 1.0};
                    }
                } else {
                    if ((input[1]) <= (32.5)) {
                        var37 = new double[]{0.8085106382978723, 0.19148936170212766};
                    } else {
                        var37 = new double[]{0.9440993788819876, 0.055900621118012424};
                    }
                }
            }
        }
        double[] var38;
        if ((input[6]) <= (0.75)) {
            if ((input[24]) <= (0.249904066324234)) {
                var38 = new double[]{0.0, 1.0};
            } else {
                var38 = new double[]{1.0, 0.0};
            }
        } else {
            if ((input[23]) <= (0.7429177165031433)) {
                if ((input[0]) <= (15.0)) {
                    var38 = new double[]{0.0, 1.0};
                } else {
                    var38 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[15]) <= (429.10748291015625)) {
                    if ((input[18]) <= (53.0)) {
                        var38 = new double[]{1.0, 0.0};
                    } else {
                        var38 = new double[]{0.9440993788819876, 0.055900621118012424};
                    }
                } else {
                    var38 = new double[]{0.8085106382978723, 0.19148936170212766};
                }
            }
        }
        double[] var39;
        if ((input[5]) <= (0.5)) {
            if ((input[19]) <= (1.1200000047683716)) {
                var39 = new double[]{1.0, 0.0};
            } else {
                if ((input[12]) <= (155.55284118652344)) {
                    if ((input[8]) <= (263.2112121582031)) {
                        var39 = new double[]{0.5846153846153846, 0.4153846153846154};
                    } else {
                        var39 = new double[]{0.0, 1.0};
                    }
                } else {
                    var39 = new double[]{0.926829268292683, 0.07317073170731707};
                }
            }
        } else {
            if ((input[17]) <= (63.600013732910156)) {
                if ((input[18]) <= (23.0)) {
                    var39 = new double[]{0.0, 1.0};
                } else {
                    var39 = new double[]{0.0, 1.0};
                }
            } else {
                if ((input[14]) <= (619.4236755371094)) {
                    if ((input[3]) <= (1.016865074634552)) {
                        var39 = new double[]{0.5135135135135135, 0.48648648648648646};
                    } else {
                        var39 = new double[]{0.8941176470588236, 0.10588235294117648};
                    }
                } else {
                    if ((input[20]) <= (3.5)) {
                        var39 = new double[]{0.0, 1.0};
                    } else {
                        var39 = new double[]{0.6785714285714286, 0.32142857142857145};
                    }
                }
            }
        }
        return mulVectorNumber(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(addVectors(var0, var1), var2), var3), var4), var5), var6), var7), var8), var9), var10), var11), var12), var13), var14), var15), var16), var17), var18), var19), var20), var21), var22), var23), var24), var25), var26), var27), var28), var29), var30), var31), var32), var33), var34), var35), var36), var37), var38), var39), 0.025);
    }
    private static double[] addVectors(double[] v1, double[] v2) {
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }
    private static double[] mulVectorNumber(double[] v1, double num) {
        double[] result = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] * num;
        }
        return result;
    }
}