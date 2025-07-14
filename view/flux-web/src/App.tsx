import { useEffect, useState } from 'react';
import FluxAppBar from './components/FluxAppBar';
import {List} from '@mui/material';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ListItemText from '@mui/material/ListItemText';
import ImageIcon from '@mui/icons-material/Image';

export default function App() {
  const [files, setFiles] = useState<string[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/files")
      .then((response) => response.json())
      .then((data) => {
        const names = data.map((item) => item.name);
        setFiles(names);
      })
      .catch((error) => console.error("Error fetching files:", error));
  }, []);

  return (
    <>
      <FluxAppBar />
      <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
        {files.map((file) => (
          <ListItem key={file}>
            <ListItemAvatar>
              <Avatar>
                <ImageIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText primary={file} secondary="Jan 9, 2014" />
          </ListItem>
        ))}
      </List>
    </>
  );
}
