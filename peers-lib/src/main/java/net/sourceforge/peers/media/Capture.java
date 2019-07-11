/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2008, 2009, 2010, 2011 Yohann Martineau 
*/

package net.sourceforge.peers.media;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.concurrent.CountDownLatch;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.rtp.RFC3551;


public class Capture implements Runnable {

    public static final int SAMPLE_SIZE = 16;
    public static final int BUFFER_SIZE = SAMPLE_SIZE * 20;

    private PipedOutputStream rawData;
    private boolean isStopped;
    private SoundSource soundSource;
    private Logger logger;
    private CountDownLatch latch;
    private Encoder encoder;
    private int payloadType;

    public Capture(PipedOutputStream rawData, SoundSource soundSource,
                   Logger logger, CountDownLatch latch, Encoder encoder, int payloadType) {
        this.rawData = rawData;
        this.soundSource = soundSource;
        this.logger = logger;
        this.latch = latch;
        this.encoder = encoder;
        isStopped = false;
        this.payloadType = payloadType;
    }

    public byte[] concatByte(byte[] b1, byte[] b2) {
        byte[] concatByte = new byte[b1.length + b2.length];

        System.arraycopy(b1, 0, concatByte, 0, b1.length);
        System.arraycopy(b2, 0, concatByte, b1.length, b2.length);

//        for (int i = 0; i < concatByte.length; i++) {
//            if (i < b1.length) {
//                concatByte[i] = b1[i];
//            } else {
//                concatByte[i] = b2[i - b1.length];
//            }
//        }
        return concatByte;
    }

    public void run() {
        byte[] buffer;

        int process_size = 160;
        byte[] result = new byte[0];

        while (!isStopped) {
            buffer = soundSource.readData();
            try {
                if (buffer == null) {
                    break;
                }
                if (payloadType == RFC3551.PAYLOAD_TYPE_G729) {
                    // result = concatByte(result, buffer);
                    // if (result.length == 320) {
                    //     rawData.write(encoder.process(result));
                    //     rawData.flush();
                    //     result = new byte[0];
                    // }
                    // if (result.length > 320) {
                    //     byte[] temp = new byte[320];
                    //     System.arraycopy(result, 0, temp, 0, 320);
                    //     rawData.write(encoder.process(temp));
                    //     rawData.flush();
                    //     byte[] temp2 = new byte[result.length - 320];
                    //     System.arraycopy(result, 320, temp2, 0, result.length - 320);
                    //     result = new byte[0];
                    //     concatByte(result, temp2);
                    // }
                    // if (buffer.length <= 0 && result.length > 0) {
                    //     rawData.write(encoder.process(result));
                    //     rawData.flush();
                    // }
                    byte[] tmp = new byte[160];
                    byte[] tmp1 = new byte[0];
                    try{
                        logger.info("buffer size=" + buffer.length);
                    if(result.length > 0){
                        logger.info("result size=" + result.length);
                        try {
                            tmp1 = new byte[result.length + buffer.length];
                            System.arraycopy(result, 0, tmp1, 0, result.length);
                            System.arraycopy(buffer, 0, tmp1, result.length, buffer.length);
                        } catch (Exception e) {
                            logger.error("encode error", e);
                        }
                        // System.exit(0);
                    }else{
                        tmp1 = new byte[buffer.length];
                        System.arraycopy(buffer, 0, tmp1, 0, buffer.length);
                    }
                    if(tmp1.length > tmp.length){
                        for(int i = 0; i < tmp1.length / tmp.length; i++){
                            System.arraycopy(tmp1, i * tmp.length, tmp, 0, tmp.length);
                            rawData.write(encoder.process(tmp));
                        }
                        rawData.flush();
                        if(tmp1.length % tmp.length > 0){
                            result = new byte[tmp1.length % tmp.length];
                            System.arraycopy(tmp1, tmp1.length - result.length, result, 0, result.length);
                        }else{
                            result = new byte[0];
                        }
                    }else if(tmp1.length < tmp.length){
                        result = tmp1;
                    }else{
                        tmp = tmp1;
                        rawData.write(encoder.process(tmp));
                        rawData.flush();
                    }
                }catch(Exception e){
                    logger.error("encode error",e);
                }


                } else {
                    rawData.write(encoder.process(buffer));
                    rawData.flush();
                }

            } catch (IOException e) {
                logger.error("input/output error", e);
                return;
            }
        }
        latch.countDown();
        if (latch.getCount() != 0) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("interrupt exception", e);
            }
        }
    }

    public synchronized void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

}
