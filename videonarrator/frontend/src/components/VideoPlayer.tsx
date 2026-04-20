'use client';
import { useEffect, useRef } from 'react';
import videojs from 'video.js';
import 'video.js/dist/video-js.css';

interface VideoPlayerProps {
  src: string;
  onTimeUpdate?: (time: number) => void;
}

export default function VideoPlayer({ src, onTimeUpdate }: VideoPlayerProps) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const playerRef = useRef<any>(null);

  useEffect(() => {
    if (!videoRef.current) return;

    playerRef.current = videojs(videoRef.current, {
      controls: true,
      fluid: true,
      sources: [{ src, type: 'video/mp4' }]
    }, () => {
      if (onTimeUpdate) {
        playerRef.current.on('timeupdate', () => {
          onTimeUpdate(playerRef.current.currentTime() * 1000); // ms
        });
      }
    });

    return () => {
      if (playerRef.current) {
        playerRef.current.dispose();
      }
    };
  }, [src, onTimeUpdate]);

  return (
    <div data-vjs-player>
      <video ref={videoRef} className="video-js vjs-big-play-centered" />
    </div>
  );
}
