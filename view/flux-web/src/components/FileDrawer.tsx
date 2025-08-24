import { useEffect, useState, useRef } from "react";
import { List } from "@mui/material";
import ListItem from "@mui/material/ListItem";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import ListItemText from "@mui/material/ListItemText";
import ImageIcon from "@mui/icons-material/Image";
import DownloadIcon from "@mui/icons-material/Download";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import { User } from "oidc-client-ts";

const API_URL: string = "http://localhost:8080/api/v1";

interface FileDTO {
  id: string;
  name: string;
  size: number;
  type: string;
  uploadedBy: string;
  status: string;
}

export default function FileDrawer() {
  const fileInput = useRef<HTMLInputElement>(null);

  const [files, setFiles] = useState<FileDTO[]>([]);

  useEffect(() => {
    fileInput.current?.addEventListener("change", (event) => {
      const target = event.target as HTMLInputElement;

      if (!target.files || target.files.length == 0) return;

      const file = target.files[0];
      console.log(1);
      addFile(file);

      target.value = "";
    });

    fetchFiles();
  }, []); // eslint-disable-line

  function getUser() {
    const authority = "http://localhost:8082/realms/myrealm";
    const client_id = "myclient";
    let oidcStorage = localStorage.getItem(
      `oidc.user:${authority}:${client_id}`,
    );
    if (!oidcStorage) {
      oidcStorage = sessionStorage.getItem(
        `oidc.user:${authority}:${client_id}`,
      );
      if (!oidcStorage) {
        return null;
      }
    }

    return User.fromStorageString(oidcStorage);
  }

  const getBearerToken = () => {
    const user = getUser();
    const access_token = user?.access_token;
    if (access_token === undefined) {
      return "Bearer";
    }
    return "Bearer " + access_token;
  };

  const addFile = async (file: File) => {
    fetch(`${API_URL}/files`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: getBearerToken(),
      },
      body: JSON.stringify({ name: file.name }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("POST /files failed");
        }
        return response.json();
      })
      .then((data) => {
        uploadFileContent(data.id, data.uploadUrl, file);
      })
      .catch((error) => console.error("Error uploading file: ", error));
  };

  const uploadFileContent = async (
    fileId: string,
    uploadUrl: string,
    file: File,
  ) => {
    fetch(uploadUrl, {
      method: "PUT",
      headers: {
        "Content-Type": "application/octet-stream",
      },
      body: file,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("PUT signed URL failed: " + response.status);
        }
        notifyFileUploaded(fileId);
        fetchFiles();
      })
      .catch((error) => console.error("Cloud Storage upload failed: ", error));
  };

  const notifyFileUploaded = async (fileId: string) => {
    fetch(`${API_URL}/files/${fileId}/upload`, {
      method: "POST",
      headers: {
        Authorization: getBearerToken(),
      },
    }).catch((error) =>
      console.error("Error notifying file uploaded: ", error),
    );
  };

  const fetchFiles = async () => {
    fetch(`${API_URL}/files`, {
      headers: {
        Authorization: getBearerToken(),
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("GET /files failed");
        }
        return response.json();
      })
      .then((data) => {
        setFiles(data);
      })
      .catch((error) => console.error("Error fetching files: ", error));
  };

  const handleDownload = async (file: FileDTO) => {
    fetch(`${API_URL}/files/${file.id}/download`, {
      headers: {
        Authorization: getBearerToken(),
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to get file download URL");
        }
        return response.text();
      })
      .then((downloadUrl) => {
        downloadFile(file.name, downloadUrl);
      })
      .catch((error) =>
        console.error("Error getting file download URL: ", error),
      );
  };

  const downloadFile = async (fileName: string, downloadUrl: string) => {
    const a = document.createElement("a");
    a.href = downloadUrl;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  return (
    <>
      <div>
        <Button variant="contained" onClick={() => fileInput.current?.click()}>
          New File
        </Button>
        <input ref={fileInput} type="file" style={{ display: "none" }} />
      </div>
      <List sx={{ width: "100%" }}>
        {files.map((file) => (
          <ListItem
            key={file.name}
            sx={{
              borderTop: "1px solid #e0e0e0",
              ":hover": { bgcolor: "grey.200" },
            }}
          >
            <ListItemAvatar>
              <Avatar>
                <ImageIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText primary={file.name} secondary="Jan 9, 2014" />
            <IconButton onClick={() => handleDownload(file)}>
              <DownloadIcon />
            </IconButton>
          </ListItem>
        ))}
      </List>
    </>
  );
}
